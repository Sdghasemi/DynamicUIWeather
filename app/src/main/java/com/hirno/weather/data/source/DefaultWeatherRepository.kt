package com.hirno.weather.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation to load weather forecast from the mocked data source.
 */
class DefaultWeatherRepository(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val remoteDataSource: WeatherDataSource,
) : WeatherRepository {
    override suspend fun getForecast() = withContext(ioDispatcher) {
        remoteDataSource.getForecast()
    }
}