package com.example.smartaq.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartaq.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val state = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        // Hà Nội ví dụ
        viewModel.loadWeather(21.0278, 105.8342)
    }

    state.value?.let { weather ->

        Column(Modifier.padding(16.dp)) {

            // CURRENT WEATHER
            Text(
                text = "Nhiệt độ hiện tại: ${weather.current_weather.temperature}°C",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(20.dp))

            // FORECAST 3 DAYS
            Text("Dự báo 3 ngày:", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(10.dp))

            for (i in 1 until 4) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        val hourlyWind = weather.hourly.windspeed_10m
                        val windAtNoon = hourlyWind[i * 24 + 12]
                        Text("Ngày: ${weather.daily.time[i]}")
                        Text("Nhiệt độ cao nhất: ${weather.daily.temperature_2m_max[i]}°C")
                        Text("Nhiệt độ thấp nhất: ${weather.daily.temperature_2m_min[i]}°C")
                        Text("Tốc độ gió: ${String.format(" % .2f", windAtNoon)} m/s")
                    }
                }
            }
        }
    }
}
