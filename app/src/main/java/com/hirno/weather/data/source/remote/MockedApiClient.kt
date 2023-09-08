package com.hirno.weather.data.source.remote

import com.hirno.weather.data.GenericResponse
import com.hirno.weather.model.ForecastDTO
import com.hirno.weather.model.UiOptionsDTO
import com.hirno.weather.model.UiOptionsDTO.BackgroundDTO
import com.hirno.weather.network.ApiClient
import com.hirno.weather.network.response.NetworkResponse
import kotlin.random.Random

/**
 * Mocked API implementation to load weather forecast info
 */
object MockedApiClient {
    private const val MOCKED_LOCATION_LATITUDE = 57.7072
    private const val MOCKED_LOCATION_LONGITUDE = 11.9668
    private const val MOCKED_LOCATION_NAME = "Västra Götaland"
    private const val HOURLY = "hourly"
    private const val TEMPERATURE = "temperature_2m"
    private const val WIND_SPEED = "windspeed_10m"
    private const val HUMIDITY = "relativehumidity_2m"
    private const val UV_INDEX = "uv_index"
    private const val DAILY = "daily"
    private const val TEMPERATURE_MAX = "temperature_2m_max"
    private const val TEMPERATURE_MIN = "temperature_2m_min"
    private const val SUNRISE = "sunrise"
    private const val SUNSET = "sunset"
    private const val PRECIPITATION_SUM = "precipitation_sum"

    private val timedRandom = Random(System.currentTimeMillis())

    suspend fun getForecast(): GenericResponse<ForecastDTO> {
        val days = provideRandomForecastDays()

        // Generate blocks visibility
        val isDay = provideRandomBoolean()
        val showLocationName = provideRandomBoolean()
        val hasNextDaysForecastBlock = days != 1
        val hasHighLowTemperatureBlock = provideRandomBoolean()
        val hasWindPrecipitationHumidityBlock = provideRandomBoolean()
        val hasUvSunriseSunsetBlock = provideRandomBoolean()
        val hasTempGraph = provideRandomBoolean()

        // Create API query params based on blocks visibility
        val hourlyVariables = mutableListOf(TEMPERATURE)
        val dailyVariables = mutableListOf<String>()
        if (hasNextDaysForecastBlock || hasHighLowTemperatureBlock) {
            dailyVariables.addAll(arrayOf(
                TEMPERATURE_MAX,
                TEMPERATURE_MIN,
            ))
        }
        if (hasWindPrecipitationHumidityBlock) {
            hourlyVariables.addAll(arrayOf(
                WIND_SPEED,
                HUMIDITY,
            ))
            dailyVariables.add(PRECIPITATION_SUM)
        }
        if (hasUvSunriseSunsetBlock) {
            hourlyVariables.add(UV_INDEX)
            dailyVariables.addAll(arrayOf(
                SUNRISE,
                SUNSET,
            ))
        }

        val hourlyQueryString = hourlyVariables.joinToString(",")
        val dailyQueryString = dailyVariables.joinToString(",")

        return ApiClient.retrofit.getForecast(
            latitude = MOCKED_LOCATION_LATITUDE,
            longitude = MOCKED_LOCATION_LONGITUDE,
            days = days,
            query = mapOf(
                HOURLY to hourlyQueryString,
                DAILY to dailyQueryString,
            ),
        ).also { response ->
            // Create UI options based on the random generated data
            (response as? NetworkResponse.Success)?.body?.apply {
                name = MOCKED_LOCATION_NAME
                ui = UiOptionsDTO(
                    background = BackgroundDTO(
                        color = provideRandomBackgroundGradientClass(),
                        direction = provideRandomBackgroundGradientDirection(),
                    ),
                    isDay = isDay,
                    showLocationName = showLocationName,
                    hasHighLowTemperatureBlock = hasHighLowTemperatureBlock,
                    blocksOrder = ArrayList<UiOptionsDTO.Block>().apply {
                        if (hasNextDaysForecastBlock) add(UiOptionsDTO.Block.NEXT_DAYS_FORECAST)
                        if (hasWindPrecipitationHumidityBlock) add(UiOptionsDTO.Block.WIND_PRECIPITATION_HUMIDITY)
                        if (hasUvSunriseSunsetBlock) add(UiOptionsDTO.Block.UV_SUNRISE_SUNSET)
                        if (hasTempGraph) add(UiOptionsDTO.Block.TEMP_GRAPH)
                    }.also { it.shuffle(timedRandom) },
                    refreshStyle = UiOptionsDTO.RefreshStyle.values().random(timedRandom),
                )
            }
        }
    }

    private fun provideRandomForecastDays() = (1..8).random(timedRandom).takeIf { it > 3 } ?: 1
    private fun provideRandomBoolean() = timedRandom.nextBoolean()
    private fun provideRandomBackgroundGradientClass() = BackgroundDTO.GradientClass.values().random(timedRandom)
    private fun provideRandomBackgroundGradientDirection() = BackgroundDTO.GradientOrientation.values().random(timedRandom)
}