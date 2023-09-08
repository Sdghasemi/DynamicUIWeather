package com.hirno.weather.model

import androidx.recyclerview.widget.RecyclerView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.hirno.weather.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Weather UI list block model
 *
 * @property viewType The view type used for [RecyclerView] items
 * @property isDay A boolean indicating the applied theme to list items
 */
sealed class WeatherListBlock(
    val viewType: Int,
    private val isDay: Boolean,
) {
    companion object {
        const val VIEW_TYPE_CURRENT_TEMP = 0
        const val VIEW_TYPE_HIGH_LOW_TEMP = 1
        const val VIEW_TYPE_NEXT_DAY_FORECAST = 2
        const val VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY = 3
        const val VIEW_TYPE_UV_SUNRISE_SUNSET = 4
        const val VIEW_TYPE_TEMP_GRAPH = 5
    }

    @DrawableRes
    val backgroundDrawableRes =
        if (isDay) R.drawable.day_list_item_background
        else R.drawable.night_list_item_background

    @ColorRes
    val primaryTextColorRes =
        if (isDay) R.color.primary_text_day
        else R.color.primary_text_night

    @ColorRes
    val secondaryTextColorRes =
        if (isDay) R.color.secondary_text_day
        else R.color.secondary_text_night

    data class CurrentTemperature(
        val locationName: String?,
        val temperature: Int,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_CURRENT_TEMP,
        isDay = isDay,
    )

    data class HighLowTemp(
        val minTemp: Int,
        val maxTemp: Int,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_HIGH_LOW_TEMP,
        isDay = isDay,
    )

    data class NextDayForecast(
        val weekDay: String,
        val minTemp: Int,
        val maxTemp: Int,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_NEXT_DAY_FORECAST,
        isDay = isDay,
    )

    data class WindSpeed(
        val speed: Double,
        val unit: String,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY,
        isDay = isDay,
    )

    data class PrecipitationSum(
        val sum: Int,
        val unit: String?,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY,
        isDay = isDay,
    )

    data class HumidityPercentage(
        val percentage: Int,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_WIND_PRECIPITATION_HUMIDITY,
        isDay = isDay,
    )

    data class UvIndex(
        val index: Int,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_UV_SUNRISE_SUNSET,
        isDay = isDay,
    )

    data class SunriseTime(
        val time: String,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_UV_SUNRISE_SUNSET,
        isDay = isDay,
    )

    data class SunsetTime(
        val time: String,
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_UV_SUNRISE_SUNSET,
        isDay = isDay,
    )

    data class TemperatureGraph(
        val timeZone: String,
        val times: List<Long> = listOf(),
        val temperatures: List<Int> = listOf(),
        val isDay: Boolean,
    ) : WeatherListBlock(
        viewType = VIEW_TYPE_TEMP_GRAPH,
        isDay = isDay,
    ) {
        val graphData = times.mapIndexed { index, time ->
            Entry(time.toFloat(), temperatures[index].toFloat())
        }
    }
}