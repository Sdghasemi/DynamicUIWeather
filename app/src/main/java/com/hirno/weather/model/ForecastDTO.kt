package com.hirno.weather.model

import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import androidx.annotation.ColorRes
import com.hirno.weather.R
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName as Key

/**
 * The response model of forecast endpoint
 */
@Parcelize
data class ForecastDTO(
    @Key("name")
    var name: String = "",
    @Key("timezone")
    val timeZone: String = "",
    @Key("hourly_units")
    val hourlyUnits: HourlyUnitsDTO = HourlyUnitsDTO(),
    @Key("hourly")
    val hourly: HourlyDTO = HourlyDTO(),
    @Key("daily_units")
    val dailyUnits: DailyUnitsDTO? = DailyUnitsDTO(),
    @Key("daily")
    val daily: DailyDTO? = DailyDTO(),
    @Key("ui_options")
    var ui: UiOptionsDTO = UiOptionsDTO(),
) : Parcelable {
    val isDay: Boolean
        get() = ui.isDay
    val nextDaysCount: Int
        get() = daily?.times?.size?.dec() ?: 0  // Not counting current day
}
@Parcelize
data class HourlyUnitsDTO(
    @Key("temperature_2m")
    var temperature: String = "",
    @Key("relativehumidity_2m")
    var relativeHumidity: String = "",
    @Key("windspeed_10m")
    var windSpeed: String = "",
) : Parcelable

@Parcelize
data class HourlyDTO(
    @Key("time")
    val times: List<Long> = listOf(),
    @Key("temperature_2m")
    val temperatures: List<Double> = listOf(),
    @Key("relativehumidity_2m")
    val relativeHumidities: List<Int> = listOf(),
    @Key("windspeed_10m")
    val windSpeeds: List<Double> = listOf(),
    @Key("uv_index")
    val uvIndices: List<Double> = listOf(),
) : Parcelable

@Parcelize
data class DailyUnitsDTO(
    @Key("temperature_2m_max")
    val temperatureMax: String = "",
    @Key("temperature_2m_min")
    val temperatureMin: String = "",
    @Key("precipitation_sum")
    val precipitationSum: String? = "",
) : Parcelable

@Parcelize
data class DailyDTO(
    @Key("time")
    val times: List<Long> = listOf(),
    @Key("temperature_2m_max")
    val maxTemperatures: List<Double> = listOf(),
    @Key("temperature_2m_min")
    val minTemperatures: List<Double> = listOf(),
    @Key("sunrise")
    val sunrises: List<Long> = listOf(),
    @Key("sunset")
    val sunsets: List<Long> = listOf(),
    @Key("precipitation_sum")
    val precipitationSums: List<Double> = listOf(),
) : Parcelable

@Parcelize
data class UiOptionsDTO(
    @Key("background")
    val background: BackgroundDTO = BackgroundDTO(),
    @Key("is_day")
    var isDay: Boolean = false,
    @Key("show_location_name")
    val showLocationName: Boolean = false,
    @Key("has_high_low_temperature_block")
    val hasHighLowTemperatureBlock: Boolean = false,
    @Key("blocks_order")
    val blocksOrder: List<Block> = listOf(),
    @Key("refresh_style")
    val refreshStyle: RefreshStyle = RefreshStyle.SWIPE,
) : Parcelable {
    @Parcelize
    data class BackgroundDTO(
        @Key("color")
        val color: GradientClass = GradientClass.CLASS_1,
        @Key("direction")
        val direction: GradientOrientation = GradientOrientation.TOP_RIGHT_TO_BOTTOM_LEFT,
    ) : Parcelable {
        @Parcelize
        enum class GradientClass(
            @ColorRes
            val start: Int,
            @ColorRes
            val end: Int,
        ) : Parcelable {
            @Key("1")
            CLASS_1(
                start = R.color.background_1_start,
                end = R.color.background_1_end,
            ),

            @Key("2")
            CLASS_2(
                start = R.color.background_2_start,
                end = R.color.background_2_end,
            ),

            @Key("3")
            CLASS_3(
                start = R.color.background_3_start,
                end = R.color.background_3_end,
            ),

            @Key("4")
            CLASS_4(
                start = R.color.background_4_start,
                end = R.color.background_4_end,
            ),

            @Key("5")
            CLASS_5(
                start = R.color.background_5_start,
                end = R.color.background_5_end,
            ),

            @Key("6")
            CLASS_6(
                start = R.color.background_6_start,
                end = R.color.background_6_end,
            ),

            @Key("7")
            CLASS_7(
                start = R.color.background_7_start,
                end = R.color.background_7_end,
            ),

            @Key("8")
            CLASS_8(
                start = R.color.background_8_start,
                end = R.color.background_8_end,
            ),
        }

        @Parcelize
        enum class GradientOrientation(
            val orientation: GradientDrawable.Orientation,
        ) : Parcelable {
            @Key("TR_BL")
            TOP_RIGHT_TO_BOTTOM_LEFT(
                orientation = GradientDrawable.Orientation.TR_BL
            ),

            @Key("BR_TL")
            BOTTOM_RIGHT_TO_TOP_LEFT(
                orientation = GradientDrawable.Orientation.BR_TL
            ),

            @Key("BL_TR")
            BOTTOM_LEFT_TO_TOP_RIGHT(
                orientation = GradientDrawable.Orientation.BL_TR
            ),

            @Key("TL_BR")
            TOP_LEFT_TO_BOTTOM_RIGHT(
                orientation = GradientDrawable.Orientation.TL_BR
            ),
        }
    }

    @Parcelize
    enum class Block : Parcelable {
        @Key("NEXT_DAYS_FORECAST")
        NEXT_DAYS_FORECAST,

        @Key("WIND_PRECIPITATION_HUMIDITY")
        WIND_PRECIPITATION_HUMIDITY,

        @Key("UV_SUNRISE_SUNSET")
        UV_SUNRISE_SUNSET,

        @Key("TEMP_GRAPH")
        TEMP_GRAPH,
    }

    @Parcelize
    enum class RefreshStyle : Parcelable {
        @Key("SWIPE")
        SWIPE,

        @Key("FAB")
        FAB,
    }
}