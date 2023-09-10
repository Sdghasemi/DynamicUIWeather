
# Dynamic Weather Forecast
Weather forecasting application that manages UI components placement, design, and visibility based on the server response. It uses a mocked location to retrieve the latest forecast from [Open-Meteo](https://open-meteo.com/) API and adds mocked UI configurations to the retrieved response. Server can control How/Where/When the UI elements should show up. The application architecture is MVI with unidirectional data flow and uses various tools and technologies to retrieve and illustrate the forecast report such as Coroutines, ViewModel, LiveData, Retrofit, OkHttp, Koin, Iconics, MPAndroidChart, etc..

## Architecture and patterns
- MVI
- Dependency Injection
- Repository Pattern

## Principles
- Clean Code
- SOLID

## Libraries
- Basic test and AppCompat libraries such as: JUnit, Espresso, UI Automator, Google Material, ConstraintLayout, SwipeRefreshLayout, Lifecycle (LiveData, ViewModels, and SavedState), Coroutines, etc.
- Retrofit with OkHttp
- Iconics for UI icons
- MPAndroidChart for graphs
- Koin for DI

## Docs
Due to the fact that the project is a sample for White-label apps, I had to make sure the UI elements and design changed significantly based on the API response, while maintaining a weather forecasting app's functionality.
[MockedApiClient](app/src/main/java/com/hirno/weather/data/source/remote/MockedApiClient.kt) is responsible for building a random mock response and returning the result. It adds a [UiOptionsDTO](app/src/main/java/com/hirno/weather/model/ForecastDTO.kt#L86) to the response which contains the UI elements configuration as follows:
- **`background:`** Controls application UI background coloring
  --- color: Gradient background color class options changeable between 8 different values
  --- direction: Gradient direction of the background changeable between 4 different values
- **`isDay:`** Boolean indicating the theming of the application. A value of `true` forces the UI to obtain light theme and a value of `false` results in a dark theme
- **`showLocationName:`** Boolean controlling whether or not the forecasting location name on the top should be visible.
- **`hasHighLowTemperatureBlock:`** Boolean controlling whether or not the current day temperature range on the top should be visible
- **`refreshStyle:`** Enum of possible page refreshing styles, changeable between "Swipe to refresh" mode and "Floating action button" press
- **`blocksOrder:`** Controls which forecast block should be visible and where should it be appeared in the screen. To hide the block it must not be in the list. The controlling blocks are as follows:
  --- Next days forecast: Can toggle visibility of future days forecast. The number of future days appearing on the screen depends on the delivered response which can be controlled with `forecast_days` query parameter of Open-Meteo API
  --- Wind, Precipitation, and Humidity: Controls visibility of different blocks with wind speed, precipitation sum, and relative humidity reports. The blocks are shown together or not shown at all.
  --- UV, Sunrise, and Sunset: Controls visibility of different blocks with UV index, sunrise time, and sunset time reports. The blocks are shown together or not shown at all.
  --- Temperature graph: Toggles visibility of the next hours temperature graph

### Example UIOption response:
Considering this example JSON with 4 days of forecast data (including current day):
```
{
  "ui_options": {
    "background": {
      "color": 3,
      "direction": "BL_TR"
    },
    "is_day": false,
    "show_location_name": true,
    "has_high_low_temperature_block": false,
    "blocks_order": [
      "TEMP_GRAPH",
      "WIND_PRECIPITATION_HUMIDITY",
      "NEXT_DAYS_FORECAST"
    ],
    "refresh_style": "FAB"
  }
}
```
It will be equivalent to this UI configuration:
- Background color: A gradient with `#D35300` to `#A70028` color codes drawn from bottom-left of the screen to its top-right corner
- Elements theme: Dark
- Shows location name on top of the current temperature
- Hides current day temperature range block
- Shows next hours temperature graph on the 3rd item
- Shows wind speed, precipitation sum, and relative humidity percentage on 4th to 6th items
- Shows next 3 days temperature range on 7th to 9th items
- Uses Floating Action Button to refresh the page

## Tests
I've included some unit tests as well as UI tests making sure the view states working as they should and the items appear on the screen based on the UI Options configuration.

## Build
The project is written in pure Kotlin and built upon AGP 8.1.1. I'm using Kotlin 1.9.0 and Gradle 8.2.1 via wrapper. [A pre-built APK](app-debug.apk) is placed in the root of the project for your convenience. To check different UI styles just refresh the page and see the result.

### Contact developer

If there's ***anything*** you'd like to discuss, feel free to contact me at [Sd.ghasemi1@gmail.com](mailto:Sd.ghasemi1@gmail.com).

Cheers🍻