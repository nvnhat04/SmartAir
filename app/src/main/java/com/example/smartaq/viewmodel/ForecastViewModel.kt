package com.example.smartaq.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartaq.data.model.ForecastRequest
import com.example.smartaq.data.model.ForecastResponse
import com.example.smartaq.data.repository.ForecastRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val repository: ForecastRepository = ForecastRepository()
) : ViewModel() {

    private val _forecast = MutableStateFlow<ForecastResponse?>(null)
    val forecast = _forecast.asStateFlow()

    fun fetchForecast(lat: Double, lon: Double, date_request: String, predays: Int, nextdays: Int) {
        viewModelScope.launch {
            try {
                val request = ForecastRequest(
                    x = lon,
                    y = lat,
                    groupcomponent_id = "63",
                    date_request = date_request,
                    predays = predays,
                    nextdays = nextdays,
                    lang_id = "vi"
                )
//                Log.d("ForecastVM", "Detect pm25")
                val response = repository.getForecast(request)
//                Log.d("ForecastVM", "Response: $response")
                _forecast.value = response

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
