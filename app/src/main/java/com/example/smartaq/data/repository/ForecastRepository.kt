package com.example.smartaq.data.repository

import com.example.smartaq.data.model.ForecastRequest



class ForecastRepository {
    suspend fun getForecast(request: ForecastRequest) =
        RetrofitClient.api.getForecast(request)
}