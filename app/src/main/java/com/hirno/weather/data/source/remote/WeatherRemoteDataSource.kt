package com.hirno.weather.data.source.remote

import com.hirno.weather.data.source.WeatherDataSource

/**
 * Weather forecast remote data source implementation
 */
object WeatherRemoteDataSource : WeatherDataSource {
    /**
     * Retrieves forecast from server
     *
     * @return Server response of the network call
     */
    override suspend fun getForecast() = MockedApiClient.getForecast()
}