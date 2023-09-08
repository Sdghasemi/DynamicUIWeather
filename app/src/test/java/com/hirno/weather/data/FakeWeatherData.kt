package com.hirno.weather.data

import com.hirno.weather.model.ForecastDTO
import com.hirno.weather.model.HourlyDTO
import com.hirno.weather.model.UiOptionsDTO

val blankForecast get() = ForecastDTO(
    name = "Test",
    hourly = HourlyDTO(
        temperatures = listOf(5.0),
    ),
    ui = UiOptionsDTO(
        isDay = true,
    )
)
val badRequest get() = ErrorDTO(
    reason = "Bad request!",
    error = true,
)