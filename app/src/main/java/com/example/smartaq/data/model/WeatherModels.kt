package com.example.smartaq.data.model

data class WeatherResponse(
    val current_weather: CurrentWeather,
    val hourly: Hourly,
    val daily: Daily
)



data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val relativehumidity_2m: List<Double>?,
    val windspeed_10m: List<Double>
)

data class Daily(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Int,
    val weathercode: Int
)


