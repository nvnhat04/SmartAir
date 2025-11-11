package com.example.smartaq.data.remote

import com.example.smartaq.data.model.ForecastRequest
import com.example.smartaq.data.model.ForecastResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ForecastApiService {
    @Headers("Content-Type: application/json")
    @POST("api/componentgeotiffdaily/identify_district_list_geotiff")
    suspend fun getForecast(@Body request: ForecastRequest): ForecastResponse
}
