package com.hirno.weather.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.hirno.weather.data.badRequest
import com.hirno.weather.data.blankForecast
import com.hirno.weather.data.source.WeatherDataSource
import com.hirno.weather.data.source.remote.FakeWeatherDataSource
import com.hirno.weather.R
import com.hirno.weather.data.fullForecast
import com.hirno.weather.model.HourlyDTO
import com.hirno.weather.model.UiOptionsDTO
import com.hirno.weather.utils.checkMatchesAtPositionOnView
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.startsWith
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@LargeTest
class WeatherActivityTest {
    private val testModules = module {
        single<WeatherDataSource> { FakeWeatherDataSource }
    }

    private lateinit var device: UiDevice

    @Before
    fun setup() {
        loadKoinModules(testModules)
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @After
    fun tearDown() {
        unloadKoinModules(testModules)
        FakeWeatherDataSource.reset()
        device.setOrientationNatural()
    }

    @Test
    fun launchActivity_activityIsLaunchedAndViewsAreShowing() {
        FakeWeatherDataSource.weather = blankForecast

        ActivityScenario.launch(WeatherActivity::class.java)

        onView(isRoot()).check(matches(isDisplayed()))
    }

    @Test
    fun errorOccursWhileScreenLoad_errorMessageIsDisplayedAndRefreshLayoutIsEnabled() {
        val message = badRequest.reason
        FakeWeatherDataSource.errorMessage = message

        ActivityScenario.launch(WeatherActivity::class.java)

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        onView(withId(R.id.list)).check(matches(not(isDisplayed())))
        onView(withText(startsWith(message))).check(matches(isDisplayed()))
        onView(withId(R.id.swipe_refresh)).check(matches(isEnabled()))
        onView(withId(R.id.fab_refresh)).check(matches(not(isDisplayed())))
    }

    @Test
    fun swipeToRefreshOnErrorState_screenGetsRefreshedIntoSuccessState() {
        val message = badRequest.reason
        FakeWeatherDataSource.errorMessage = message

        ActivityScenario.launch(WeatherActivity::class.java)

        FakeWeatherDataSource.apply {
            reset()
            weather = blankForecast
        }

        onView(withId(R.id.swipe_refresh)).perform(swipeDown())
        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun launchActivity_forecastViewsAreVisibleWithCorrectValues() {
        val model = fullForecast
        FakeWeatherDataSource.weather = model

        ActivityScenario.launch(WeatherActivity::class.java)

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())))
        onView(withId(R.id.message)).check(matches(not(isDisplayed())))
        onView(withId(R.id.fab_refresh)).check(matches(not(isDisplayed())))
        onView(withId(R.id.swipe_refresh)).check(matches(isEnabled()))
        onView(withText(model.name)).check(matches(isDisplayed()))
        onView(withId(R.id.list)).apply {
            for (position in 0 .. 14) when (position) {
                0 -> checkMatchesAtPositionOnView(
                    position = position,
                    targetViewId = R.id.temp,
                    matcher = withText(startsWith(model.hourly.temperatures.first().toInt().toString()))
                )
                1 -> {
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.high_temp,
                        matcher = withText(startsWith(model.daily!!.maxTemperatures.first().toInt().toString()))
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.low_temp,
                        matcher = withText(startsWith(model.daily!!.minTemperatures.first().toInt().toString()))
                    )
                }
                in 2 .. 7 -> {
                    val nextDayIdx = position - 1
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.weekday,
                        matcher = withText(
                            when (nextDayIdx) {
                                1 -> "Sat"
                                2 -> "Sun"
                                3 -> "Mon"
                                4 -> "Tue"
                                5 -> "Wed"
                                6 -> "Thu"
                                else -> null
                            }
                        )
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.high_temp,
                        matcher = withText(startsWith(model.daily!!.maxTemperatures[nextDayIdx].toInt().toString()))
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.low_temp,
                        matcher = withText(startsWith(model.daily!!.minTemperatures[nextDayIdx].toInt().toString()))
                    )
                }
                8 -> {
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.number,
                        matcher = withText(model.hourly.windSpeeds.first().toInt().toString())
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.unit,
                        matcher = withText(model.hourlyUnits.windSpeed)
                    )
                }
                9 -> {
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.number,
                        matcher = withText(model.daily!!.precipitationSums.first().toInt().toString())
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.unit,
                        matcher = withText(model.dailyUnits!!.precipitationSum)
                    )
                }
                10 -> {
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.number,
                        matcher = withText(model.hourly.relativeHumidities.first().toInt().toString())
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.unit,
                        matcher = withText(model.hourlyUnits.relativeHumidity)
                    )
                }
                11 -> {
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.title,
                        matcher = withText(R.string.un_index)
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.number,
                        matcher = withText(model.hourly.uvIndices.first().toInt().toString())
                    )
                }
                12 -> {
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.title,
                        matcher = withText(R.string.sunrise)
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.number,
                        matcher = withText(model.daily!!.sunrises.first().to12HourTime(model.timeZone))
                    )
                }
                13 -> {
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.title,
                        matcher = withText(R.string.sunset)
                    )
                    checkMatchesAtPositionOnView(
                        position = position,
                        targetViewId = R.id.number,
                        matcher = withText(model.daily!!.sunsets.first().to12HourTime(model.timeZone))
                    )
                }
                14 -> checkMatchesAtPositionOnView(
                    position = position,
                    targetViewId = R.id.graph,
                    matcher = isDisplayed()
                )
            }
        }
    }

    @Test
    fun setSuccessStateWithFabRefreshStyle_SwipeToRefreshIsDisabledAndFabIsVisible() {
        FakeWeatherDataSource.weather = fullForecast.copy(
            ui = UiOptionsDTO(
                refreshStyle = UiOptionsDTO.RefreshStyle.FAB
            )
        )

        ActivityScenario.launch(WeatherActivity::class.java)

        onView(withId(R.id.swipe_refresh)).check(matches(not(isEnabled())))
        onView(withId(R.id.fab_refresh)).check(matches(isDisplayed()))
    }

    @Test
    fun rotateScreenOnErrorState_forecastInfoLoadedOnScreenLoad() {
        FakeWeatherDataSource.errorMessage = badRequest.reason

        ActivityScenario.launch(WeatherActivity::class.java)

        onView(withId(R.id.list)).check(matches(not(isDisplayed())))

        FakeWeatherDataSource.apply {
            reset()
            weather = blankForecast
        }

        device.setOrientationLandscape()

        onView(withId(R.id.list)).check(matches(isDisplayed()))
    }

    @Test
    fun rotateScreenOnSuccessState_screenLoadMaintainsSuccessState() {
        FakeWeatherDataSource.weather = blankForecast

        ActivityScenario.launch(WeatherActivity::class.java)

        onView(withText(startsWith(blankForecast.hourly.temperatures.first().toInt().toString()))).check(matches(isDisplayed()))

        FakeWeatherDataSource.weather = blankForecast.copy(
            hourly = HourlyDTO(
                temperatures = listOf(
                    2.0
                ),
            ),
        )

        device.setOrientationLandscape()

        onView(withText(startsWith(blankForecast.hourly.temperatures.first().toInt().toString()))).check(matches(isDisplayed()))
    }

    private fun Long.to12HourTime(timeZone: String): String =
        SimpleDateFormat("hh:mm a", Locale.ENGLISH).apply {
            setTimeZone(TimeZone.getTimeZone(timeZone))
        }.format(this * 1000)
}