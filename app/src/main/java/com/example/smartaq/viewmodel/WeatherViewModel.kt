package com.example.smartaq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartaq.data.model.DailyUI
import com.example.smartaq.data.model.WeatherResponse
import com.example.smartaq.data.repository.WeatherRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherResponse?>(null)
    val uiState = _uiState.asStateFlow()

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val data = repository.getWeather(lat, lon)
                _uiState.value = data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



}
