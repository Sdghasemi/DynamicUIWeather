package com.hirno.weather.data.source

import com.hirno.weather.data.GenericResponse
import com.hirno.weather.model.ForecastDTO

/**
 * Main entry point for accessing weather forecast data.
 */
interface WeatherDataSource {
    suspend fun getForecast(): GenericResponse<ForecastDTO>
}