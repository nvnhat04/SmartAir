package com.example.smartaq.data.repository


import com.example.smartaq.data.model.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class WeatherRepository {

    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        val url = "https://api.open-meteo.com/v1/forecast" +
                "?latitude=$lat&longitude=$lon" +
                "&current_weather=true" +
                "&hourly=temperature_2m,relativehumidity_2m,precipitation,windspeed_10m" +
                "&daily=temperature_2m_max,temperature_2m_min,uv_index_max" +
                "&timezone=auto"

        val json = withContext(Dispatchers.IO) {
            URL(url).readText()
        }

        return Gson().fromJson(json, WeatherResponse::class.java)
    }
}
