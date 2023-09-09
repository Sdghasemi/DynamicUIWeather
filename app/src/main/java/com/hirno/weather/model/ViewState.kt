package com.hirno.weather.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.hirno.weather.model.WeatherListBlock.CurrentTemperature
import com.hirno.weather.model.WeatherListBlock.HumidityPercentage
import com.hirno.weather.model.WeatherListBlock.HighLowTemp
import com.hirno.weather.model.WeatherListBlock.NextDayForecast
import com.hirno.weather.model.WeatherListBlock.PrecipitationSum
import com.hirno.weather.model.WeatherListBlock.SunriseTime
import com.hirno.weather.model.WeatherListBlock.SunsetTime
import com.hirno.weather.model.WeatherListBlock.TemperatureGraph
import com.hirno.weather.model.WeatherListBlock.UvIndex
import com.hirno.weather.model.WeatherListBlock.WindSpeed
import com.hirno.weather.ui.WeatherActivity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * States of the [WeatherActivity] screen
 */
sealed class WeatherState {
    @Parcelize
    data object Loading : WeatherState(), Parcelable

    @Parcelize
    data class Error(
        val text: String? = null,
        @StringRes
        val resId: Int? = null,
        private val _refreshStyle: UiOptionsDTO.RefreshStyle? = null,
    ) : WeatherState(), Parcelable {
        companion object {
            fun from(error: Any, previousState: WeatherState? = null): Error {
                val refreshStyle = when (previousState) {
                    is Success -> previousState.weather.ui.refreshStyle
                    is Error -> previousState.refreshStyle
                    else -> null
                }
                return when (error) {
                    is Int -> Error(resId = error, _refreshStyle = refreshStyle)
                    is String -> Error(text = error, _refreshStyle = refreshStyle)
                    else -> throw IllegalArgumentException("error message must be String or Int")
                }
            }
        }

        val refreshStyle: UiOptionsDTO.RefreshStyle
            get() = _refreshStyle ?: UiOptionsDTO.RefreshStyle.SWIPE
    }

    @Parcelize
    data class Success(
        val weather: ForecastDTO,
    ) : WeatherState(), Parcelable {
        @IgnoredOnParcel
        val listBlocks: List<WeatherListBlock> = buildList {
            with(weather) {
                add(
                    CurrentTemperature(
                        locationName = name.takeIf { ui.showLocationName },
                        temperature = hourly.temperatures.first().toInt(),
                        isDay = isDay,
                    )
                )
                if (ui.hasHighLowTemperatureBlock) add(
                    HighLowTemp(
                        maxTemp = daily!!.maxTemperatures.first().toInt(),
                        minTemp = daily.minTemperatures.first().toInt(),
                        isDay = isDay,
                    )
                )
                ui.blocksOrder.forEach { block ->
                    when (block) {
                        UiOptionsDTO.Block.NEXT_DAYS_FORECAST -> {
                            for (index in 1 until daily!!.times.size) {
                                add(
                                    NextDayForecast(
                                        weekDay = daily.times[index].toWeekDay(timeZone),
                                        maxTemp = daily.maxTemperatures[index].toInt(),
                                        minTemp = daily.minTemperatures[index].toInt(),
                                        isDay = isDay,
                                    )
                                )
                            }
                        }

                        UiOptionsDTO.Block.WIND_PRECIPITATION_HUMIDITY -> {
                            for (index in 0..2) add(
                                when (index) {
                                    0 -> WindSpeed(
                                        speed = hourly.windSpeeds.first(),
                                        unit = hourlyUnits.windSpeed,
                                        isDay = isDay,
                                    )

                                    1 -> PrecipitationSum(
                                        sum = daily!!.precipitationSums.first().toInt(),
                                        unit = dailyUnits!!.precipitationSum,
                                        isDay = isDay,
                                    )

                                    else -> HumidityPercentage(
                                        percentage = hourly.relativeHumidities.first(),
                                        isDay = isDay,
                                    )
                                }
                            )
                        }

                        UiOptionsDTO.Block.UV_SUNRISE_SUNSET -> {
                            for (index in 0..2) add(
                                when (index) {
                                    0 -> UvIndex(
                                        index = hourly.uvIndices.first().toInt(),
                                        isDay = isDay,
                                    )

                                    1 -> SunriseTime(
                                        time = daily!!.sunrises.first().to12HourTime(timeZone),
                                        isDay = isDay,
                                    )

                                    else -> SunsetTime(
                                        time = daily!!.sunsets.first().to12HourTime(timeZone),
                                        isDay = isDay,
                                    )
                                }
                            )
                        }

                        UiOptionsDTO.Block.TEMP_GRAPH -> add(
                            TemperatureGraph(
                                timeZone = timeZone,
                                times = hourly.times
                                    .let { it.subList(0, minOf(24, it.size)) },
                                temperatures = hourly.temperatures
                                    .let { it.subList(0, minOf(24, it.size)) }
                                    .map { it.toInt() },
                                isDay = isDay,
                            )
                        )
                    }
                }
            }
        }
    }

    protected fun Long.toWeekDay(timeZone: String): String =
        SimpleDateFormat("EEE", Locale.ENGLISH).apply {
            setTimeZone(TimeZone.getTimeZone(timeZone))
        }.format(this * 1000)

    protected fun Long.to12HourTime(timeZone: String): String =
        SimpleDateFormat("hh:mm a", Locale.ENGLISH).apply {
            setTimeZone(TimeZone.getTimeZone(timeZone))
        }.format(this * 1000)
}

/**
 * Events occurring in [WeatherActivity] screen
 */
sealed class WeatherEvent {
    data object ScreenLoad : WeatherEvent()
    data object Refresh : WeatherEvent()
}