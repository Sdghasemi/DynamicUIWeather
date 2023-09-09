package com.hirno.weather.data

import com.hirno.weather.model.DailyDTO
import com.hirno.weather.model.DailyUnitsDTO
import com.hirno.weather.model.ForecastDTO
import com.hirno.weather.model.HourlyDTO
import com.hirno.weather.model.HourlyUnitsDTO
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
val fullForecast get() = ForecastDTO(
    name = "San Fransisco, CA",
    timeZone = "America/Los_Angeles",
    hourlyUnits = HourlyUnitsDTO(
        temperature = "°C",
        relativeHumidity = "%",
        windSpeed = "km/h",
    ),
    hourly = HourlyDTO(
        times = listOf(1694156400,1694160000,1694163600,1694167200,1694170800,1694174400,1694178000,1694181600,1694185200,1694188800,1694192400,1694196000,1694199600,1694203200,1694206800,1694210400,1694214000,1694217600,1694221200,1694224800,1694228400,1694232000,1694235600,1694239200,1694242800,1694246400,1694250000,1694253600,1694257200,1694260800,1694264400,1694268000,1694271600,1694275200,1694278800,1694282400,1694286000,1694289600,1694293200,1694296800,1694300400,1694304000,1694307600,1694311200,1694314800,1694318400,1694322000,1694325600,1694329200,1694332800,1694336400,1694340000,1694343600,1694347200,1694350800,1694354400,1694358000,1694361600,1694365200,1694368800,1694372400,1694376000,1694379600,1694383200,1694386800,1694390400,1694394000,1694397600,1694401200,1694404800,1694408400,1694412000,1694415600,1694419200,1694422800,1694426400,1694430000,1694433600,1694437200,1694440800,1694444400,1694448000,1694451600,1694455200,1694458800,1694462400,1694466000,1694469600,1694473200,1694476800,1694480400,1694484000,1694487600,1694491200,1694494800,1694498400,1694502000,1694505600,1694509200,1694512800,1694516400,1694520000,1694523600,1694527200,1694530800,1694534400,1694538000,1694541600,1694545200,1694548800,1694552400,1694556000,1694559600,1694563200,1694566800,1694570400,1694574000,1694577600,1694581200,1694584800,1694588400,1694592000,1694595600,1694599200,1694602800,1694606400,1694610000,1694613600,1694617200,1694620800,1694624400,1694628000,1694631600,1694635200,1694638800,1694642400,1694646000,1694649600,1694653200,1694656800,1694660400,1694664000,1694667600,1694671200,1694674800,1694678400,1694682000,1694685600,1694689200,1694692800,1694696400,1694700000,1694703600,1694707200,1694710800,1694714400,1694718000,1694721600,1694725200,1694728800,1694732400,1694736000,1694739600,1694743200,1694746800,1694750400,1694754000,1694757600,),
        temperatures = listOf(23.3,16.7,18.3,20.7,22.3,23.5,23.6,24.0,23.8,23.3,22.4,21.1,20.1,19.3,18.6,18.3,17.9,17.8,17.9,17.9,17.5,17.2,17.1,17.8,18.7,20.0,21.4,23.1,24.4,25.4,25.7,25.4,25.1,24.6,23.7,22.5,21.2,20.2,19.7,19.2,18.7,18.3,17.9,17.5,16.6,16.1,15.8,15.9,16.2,16.6,17.0,17.4,18.0,19.0,19.6,19.9,20.3,19.6,18.7,17.8,17.2,16.9,16.9,16.8,16.8,16.8,16.8,16.1,16.1,16.2,16.3,17.1,18.2,19.4,20.4,21.0,20.9,21.1,21.2,20.9,20.5,19.8,19.3,18.6,18.4,18.4,18.3,18.2,18.2,18.1,18.1,18.1,18.1,18.0,17.9,17.9,18.2,18.5,18.9,19.3,19.7,20.0,20.1,20.1,19.9,19.4,18.7,18.1,17.7,17.5,17.4,17.5,17.6,17.5,17.0,16.3,15.7,15.3,15.1,15.0,15.4,16.0,16.5,16.6,16.5,16.4,16.4,16.0,15.4,14.5,13.4,12.5,11.9,11.4,11.0,10.8,10.6,10.5,10.2,10.0,9.8,9.7,9.7,9.9,10.7,11.7,12.6,13.2,13.6,13.9,14.1,14.1,13.8,12.8,11.5,10.3,9.4,8.7,8.2,7.8,7.6,7.4,6.8,6.2,5.9,5.7,5.7,6.5),
        relativeHumidities = listOf(91,84,77,69,65,62,63,62,65,64,64,73,80,84,86,85,83,85,84,82,83,83,82,79,75,72,68,63,57,49,47,51,55,57,61,65,69,76,79,82,85,88,90,92,97,98,98,97,95,93,91,88,83,79,78,77,77,80,84,90,94,96,96,94,93,92,92,99,99,99,100,98,93,87,82,80,81,80,79,77,77,80,84,91,93,94,95,96,96,96,96,97,97,96,95,94,93,92,90,86,81,78,77,77,78,82,87,91,93,95,95,94,92,90,88,85,83,82,81,79,74,67,61,58,56,55,56,56,58,62,68,73,75,76,77,78,78,79,80,80,81,82,83,82,77,69,62,57,54,51,49,49,51,56,64,70,73,75,76,77,78,79,81,84,87,90,93,92),
        windSpeeds = listOf(8.3,8.3,9.7,9.0,8.6,6.5,7.9,5.8,8.3,8.3,8.3,7.6,7.2,5.8,6.8,8.3,6.8,10.1,10.4,9.7,9.7,9.7,10.1,11.5,11.9,11.2,10.8,12.2,13.3,15.1,15.5,14.8,14.4,13.3,11.2,11.2,9.7,9.7,9.4,7.9,7.6,8.6,7.2,4.7,5.4,6.8,5.0,6.1,6.1,6.8,7.9,9.7,9.4,9.4,10.4,10.1,9.4,10.1,8.6,7.9,7.6,7.2,7.2,5.8,4.3,3.6,3.6,4.4,4.7,5.2,6.2,8.4,12.6,13.3,16.9,16.7,15.3,15.9,16.3,16.2,15.5,14.0,14.6,13.1,12.4,11.9,11.5,10.8,10.5,9.8,9.3,8.9,8.9,8.7,8.8,9.1,10.3,11.7,13.1,13.6,13.5,13.4,13.0,13.1,13.1,13.4,13.3,13.0,12.2,10.8,10.2,11.1,13.8,15.3,14.1,12.1,10.9,10.8,10.8,11.2,11.9,12.9,14.0,14.7,15.3,15.9,14.9,15.0,13.9,11.4,7.7,4.7,3.3,2.5,1.8,0.5,0.8,1.9,2.3,2.3,2.1,0.5,2.2,4.5,6.4,7.5,8.8,9.5,10.0,10.3,10.4,10.7,10.1,8.3,5.6,3.6,2.4,2.5,2.6,2.1,1.8,2.5,3.1,3.4,3.7,3.9,4.1,4.2),
        uvIndices = listOf(7.15,1.95,2.75,3.50,3.85,3.60,3.55,2.60,1.85,1.15,0.55,0.10,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.35,0.90,1.65,2.40,3.25,3.65,3.70,3.30,2.80,1.95,1.20,0.50,0.10,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.35,0.95,1.75,2.90,3.55,4.05,4.15,3.80,3.15,2.25,1.35,0.55,0.05,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.30,0.85,1.65,2.25,3.05,3.45,3.60,3.75,3.10,2.05,1.05,0.45,0.05,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.30,0.80,1.20,2.10,1.50,0.85,0.75,1.70,2.30,1.90,1.20,0.15,0.05,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.05,0.10,0.20,0.25,0.30,0.35,0.45,0.65,0.90,1.05,0.95,0.75,0.55,0.35,0.15,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.10,0.50,1.05,1.65,2.40,3.20,3.65,3.65,3.30,2.80,2.10,1.25,0.55,0.20,0.10,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.05),
    ),
    dailyUnits = DailyUnitsDTO(
        temperatureMin = "°C",
        temperatureMax = "°C",
        precipitationSum = "mm",
    ),
    daily = DailyDTO(
        times = listOf(1694156400,1694242800,1694329200,1694415600,1694502000,1694588400,1694674800),
        maxTemperatures = listOf(24.0,25.7,20.3,21.2,20.1,16.6,14.1),
        minTemperatures = listOf(13.3,15.8,16.1,17.9,12.0,9.7,5.7),
        sunrises = listOf(1694147235,1694233762,1694320288,1694406814,1694493340,1694579866,1694666391),
        sunsets = listOf(1694195548,1694281779,1694368011,1694454243,1694540476,1694626709,1694712943),
        precipitationSums = listOf(0.0,0.0,0.0,6.40,3.0,0.0,0.0),
    ),
    ui = UiOptionsDTO(
        background = UiOptionsDTO.BackgroundDTO(
            color = UiOptionsDTO.BackgroundDTO.GradientClass.CLASS_1,
            direction = UiOptionsDTO.BackgroundDTO.GradientOrientation.BOTTOM_LEFT_TO_TOP_RIGHT,
        ),
        isDay = true,
        showLocationName = true,
        hasHighLowTemperatureBlock = true,
        blocksOrder = listOf(
            UiOptionsDTO.Block.NEXT_DAYS_FORECAST,
            UiOptionsDTO.Block.WIND_PRECIPITATION_HUMIDITY,
            UiOptionsDTO.Block.UV_SUNRISE_SUNSET,
            UiOptionsDTO.Block.TEMP_GRAPH,
        ),
        refreshStyle = UiOptionsDTO.RefreshStyle.SWIPE,
    )
)
val badRequest get() = ErrorDTO(
    reason = "Bad request!",
    error = true,
)