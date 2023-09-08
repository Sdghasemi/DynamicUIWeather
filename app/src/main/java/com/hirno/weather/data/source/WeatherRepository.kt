package com.hirno.weather.data.source

import com.hirno.weather.data.GenericResponse
import com.hirno.weather.model.ForecastDTO

/**
 * Interface to the data layer.
 */
interface WeatherRepository {
    suspend fun getForecast(): GenericResponse<ForecastDTO>
}