package com.hirno.weather.viewmodel

import androidx.lifecycle.*
import com.hirno.weather.ui.WeatherActivity
import com.hirno.weather.R
import com.hirno.weather.data.source.WeatherRepository
import com.hirno.weather.model.WeatherEvent
import com.hirno.weather.model.WeatherState
import com.hirno.weather.network.response.NetworkResponse
import com.hirno.weather.utils.liveData
import kotlinx.coroutines.launch

/**
 * The [WeatherActivity] view model. Stores and manipulates state of the activity
 *
 * @property repository The repository instance used to manage forecast info
 */
class WeatherViewModel(
    savedState: SavedStateHandle,
    private val repository: WeatherRepository,
) : ViewModel() {

    private val viewState: MutableLiveData<WeatherState> by savedState.liveData()

    val obtainState: LiveData<WeatherState> = viewState

    fun event(event: WeatherEvent) {
        when(event) {
            WeatherEvent.ScreenLoad -> {
                if (viewState.value !is WeatherState.Success)
                    loadWeatherForecast()
            }
            WeatherEvent.Refresh -> loadWeatherForecast()
        }
    }

    private fun loadWeatherForecast() {
        viewModelScope.launch {
            viewState.apply {
                val previousState = value
                value = WeatherState.Loading
                value = when (val result = repository.getForecast()) {
                    is NetworkResponse.Success -> WeatherState.Success(weather = result.body)
                    is NetworkResponse.ApiError -> WeatherState.Error.from(
                        error = result.body.reason ?: R.string.an_error_occurred,
                        previousState = previousState
                    )
                    is NetworkResponse.NetworkError -> WeatherState.Error.from(
                        error = R.string.failed_to_connect_to_remote_server,
                        previousState = previousState
                    )
                    is NetworkResponse.UnknownError -> WeatherState.Error.from(
                        error = R.string.an_error_occurred,
                        previousState = previousState
                    )
                }
            }
        }
    }
}