package com.hirno.weather.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirno.weather.data.ErrorDTO
import com.hirno.weather.data.badRequest
import com.hirno.weather.data.blankForecast
import com.hirno.weather.data.source.WeatherDataSource
import com.hirno.weather.data.source.remote.FakeWeatherDataSource
import com.hirno.weather.di.testAppModules
import com.hirno.weather.model.UiOptionsDTO
import com.hirno.weather.model.WeatherEvent
import com.hirno.weather.model.WeatherState
import com.hirno.weather.utils.MainDispatcherRule
import com.hirno.weather.utils.getOrAwaitValue
import com.hirno.weather.utils.skip
import kotlinx.coroutines.CoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class WeatherViewModelTest : KoinTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        testAppModules(
            module {
                single<CoroutineDispatcher>(
                    qualifier = named("IODispatcher")
                ) {
                    mainDispatcherRule.testDispatcher
                }
                single { SavedStateHandle() }
                single<WeatherDataSource> { FakeWeatherDataSource }
            }
        )
    }

    private val viewModel: WeatherViewModel by inject()
    private val savedState: SavedStateHandle by inject()

    @Before
    fun setup() {
        FakeWeatherDataSource.reset()
    }

    @Test
    fun `test loading view state`() {
        val viewState = viewModel.obtainState.getOrAwaitValue {
            viewModel.event(WeatherEvent.ScreenLoad)
        }

        assertEquals(
            expected = WeatherState.Loading,
            actual = viewState,
        )
    }

    @Test
    fun `test success view state`() {
        FakeWeatherDataSource.weather = blankForecast

        val response = viewModel.obtainState
            .skip { it is WeatherState.Loading }
            .getOrAwaitValue {
                viewModel.event(WeatherEvent.ScreenLoad)
            }

        assertEquals(
            expected = WeatherState.Success(blankForecast),
            actual = response,
        )
    }

    @Test
    fun `test error view state`() {
        val error = badRequest.message
        FakeWeatherDataSource.errorMessage = error

        val response = viewModel.obtainState
            .skip { it is WeatherState.Loading }
            .getOrAwaitValue {
                viewModel.event(WeatherEvent.ScreenLoad)
            }

        assertEquals(
            expected = WeatherState.Error.from(error = error),
            actual = response,
        )
    }

    @Test
    fun `test refresh event`() {
        viewModel.event(WeatherEvent.ScreenLoad)

        var response = viewModel.obtainState
            .skip { it is WeatherState.Success }
            .getOrAwaitValue {
                viewModel.event(WeatherEvent.Refresh)
            }

        assertEquals(
            expected = WeatherState.Loading,
            actual = response,
        )

        response = viewModel.obtainState.getOrAwaitValue()

        assertEquals(
            expected = WeatherState.Success(blankForecast),
            actual = response,
        )
    }

    @Test
    fun `test refresh event after error`() {
        FakeWeatherDataSource.errorMessage = badRequest.message

        viewModel.event(WeatherEvent.ScreenLoad)

        FakeWeatherDataSource.reset()

        var response = viewModel.obtainState
            .skip { it is WeatherState.Error }
            .getOrAwaitValue {
                viewModel.event(WeatherEvent.Refresh)
            }

        assertEquals(
            expected = WeatherState.Loading,
            actual = response,
        )

        response = viewModel.obtainState.getOrAwaitValue()

        assertEquals(
            expected = WeatherState.Success(blankForecast),
            actual = response,
        )
    }

    @Test
    fun `test saved error state`() {
        val error = badRequest.message
        FakeWeatherDataSource.errorMessage = error

        viewModel.event(WeatherEvent.ScreenLoad)

        assertEquals(
            expected = WeatherState.Error.from(error = error),
            actual = savedState["viewState"],
        )
    }

    @Test
    fun `test saved success state`() {
        FakeWeatherDataSource.weather = blankForecast

        viewModel.event(WeatherEvent.ScreenLoad)

        assertEquals(
            expected = WeatherState.Success(blankForecast),
            actual = savedState["viewState"],
        )
    }

    @Test
    fun `test preserving previous refresh style on error after success`() {
        FakeWeatherDataSource.weather = blankForecast.copy(
            ui = UiOptionsDTO(
                refreshStyle = UiOptionsDTO.RefreshStyle.FAB,
            )
        )

        viewModel.event(WeatherEvent.ScreenLoad)

        FakeWeatherDataSource.apply {
            reset()
            errorMessage = badRequest.message
        }

        viewModel.event(WeatherEvent.Refresh)

        val response = viewModel.obtainState
            .skip { it is WeatherState.Loading }
            .getOrAwaitValue()

        assertEquals(
            expected = UiOptionsDTO.RefreshStyle.FAB,
            actual = (response as WeatherState.Error).refreshStyle,
        )
    }

    @Test
    fun `test changing failed response after calling ScreenLoad again`() {
        FakeWeatherDataSource.errorMessage = badRequest.message

        viewModel.event(WeatherEvent.ScreenLoad)

        FakeWeatherDataSource.apply {
            reset()
            weather = blankForecast
        }

        viewModel.event(WeatherEvent.ScreenLoad)

        val response = viewModel.obtainState
            .skip { it is WeatherState.Loading }
            .getOrAwaitValue()

        assertEquals(
            expected = WeatherState.Success(blankForecast),
            actual = response,
        )
    }

    @Test
    fun `test maintaining old success state after calling ScreenLoad again`() {
        FakeWeatherDataSource.weather = blankForecast

        viewModel.event(WeatherEvent.ScreenLoad)

        FakeWeatherDataSource.weather = blankForecast.copy(
            name = "Test again"
        )

        viewModel.event(WeatherEvent.ScreenLoad)

        val response = viewModel.obtainState
            .skip { it is WeatherState.Loading }
            .getOrAwaitValue()

        assertEquals(
            expected = WeatherState.Success(blankForecast),
            actual = response,
        )
    }

    private val ErrorDTO.message: String
        get() = reason ?: ""
}