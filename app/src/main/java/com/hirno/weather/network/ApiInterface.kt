package com.hirno.weather.network

import com.hirno.weather.data.GenericResponse
import com.hirno.weather.model.ForecastDTO
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * API interface of the app endpoints
 */
interface ApiInterface {
    @GET("forecast")
    suspend fun getForecast(
        @Query("latitude")
        latitude: Double,
        @Query("longitude")
        longitude: Double,
        @Query("timeformat")
        timeFormat: String = "unixtime",
        @Query("timezone")
        timezone: String = "Europe/Berlin",
        @Query("forecast_days")
        days: Int,
        @QueryMap(encoded = true)
        query: Map<String, String>,
    ): GenericResponse<ForecastDTO>
}