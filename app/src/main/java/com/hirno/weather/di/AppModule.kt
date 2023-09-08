package com.hirno.weather.di

import com.hirno.weather.data.source.DefaultWeatherRepository
import com.hirno.weather.data.source.WeatherDataSource
import com.hirno.weather.data.source.WeatherRepository
import com.hirno.weather.data.source.remote.WeatherRemoteDataSource
import com.hirno.weather.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Koin dependency injection modules declaration
 */
val appModule = module {
    single(named("IODispatcher")) {
        Dispatchers.IO
    }
    single<WeatherDataSource> { WeatherRemoteDataSource }
    single<WeatherRepository> { DefaultWeatherRepository(get(named("IODispatcher")), get()) }
    viewModel { WeatherViewModel(get(), get()) }
}