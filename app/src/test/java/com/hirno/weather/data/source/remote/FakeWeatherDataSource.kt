package com.hirno.weather.data.source.remote

import com.hirno.weather.data.ErrorDTO
import com.hirno.weather.data.blankForecast
import com.hirno.weather.data.source.WeatherDataSource
import com.hirno.weather.network.response.NetworkResponse

object FakeWeatherDataSource : WeatherDataSource {
    var weather = blankForecast
    var errorMessage: String? = null

    fun reset() {
        weather = blankForecast
        errorMessage = null
    }

    override suspend fun getForecast() = generateResponse()

    private fun generateResponse() = errorMessage?.let { message ->
        NetworkResponse.ApiError(
            body = ErrorDTO(
                reason = message,
                error = true,
            ),
            code = 400
        )
    } ?: NetworkResponse.Success(weather)
}