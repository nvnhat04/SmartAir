package com.example.smartaq.data.remote



import com.example.smartaq.data.model.ForecastResponse
import com.example.smartaq.data.model.ForecastRequest
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("forecast")   // thay bằng endpoint thật của bạn
    suspend fun getForecast(@Body request: ForecastRequest): ForecastResponse
}
