package com.hirno.weather.data.source.remote

import com.hirno.weather.data.ErrorResponseModel
import com.hirno.weather.data.GenericResponse
import com.hirno.weather.data.source.WeatherDataSource
import com.hirno.weather.model.ForecastDTO
import com.hirno.weather.network.response.NetworkResponse

object FakeWeatherDataSource : WeatherDataSource {
    var weather = ForecastDTO()
    var errorMessage: String? = null
    override suspend fun getForecast() = generateResponse()

    private fun generateResponse() = errorMessage?.let { message ->
        NetworkResponse.ApiError(
            body = ErrorResponseModel(
                reason = message,
                error = true,
            ),
            code = 400
        )
    } ?: NetworkResponse.Success(weather)
}