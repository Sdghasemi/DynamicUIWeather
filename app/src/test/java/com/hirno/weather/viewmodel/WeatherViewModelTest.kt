package com.hirno.weather.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirno.weather.TestApplication
import com.hirno.weather.data.source.WeatherDataSource
import com.hirno.weather.data.source.remote.FakeWeatherDataSource
import com.hirno.weather.di.testAppModules
import com.hirno.weather.model.ForecastDTO
import com.hirno.weather.model.UiOptionsDTO
import com.hirno.weather.model.WeatherEvent
import com.hirno.weather.model.WeatherState
import com.hirno.weather.network.response.NetworkResponse
import com.hirno.weather.utils.getOrAwaitValue
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
class WeatherViewModelTest : KoinTest {
    @get:Rule
    val koinTestRule = KoinTestRule.create {
        testAppModules(
            module {
                single<WeatherDataSource> { FakeWeatherDataSource }
            }
        )
    }

    private val viewModel: WeatherViewModel by inject()

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test success view state`() {
        val weather = ForecastDTO(
            name = "Test",
            ui = UiOptionsDTO(
                isDay = true,
            )
        )
        FakeWeatherDataSource.weather = weather

        val weatherResponse = viewModel.obtainState.getOrAwaitValue {
            viewModel.event(WeatherEvent.ScreenLoad)
        }

        assertEquals(
            expected = WeatherState.Success(weather),
            actual = weatherResponse,
        )
    }
}