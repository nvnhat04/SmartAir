package com.example.smartaq.data.model


data class ForecastRequest(
    val x: Double,
    val y: Double,
    val groupcomponent_id: String,
    val date_request: String,
    val predays: Int,
    val nextdays: Int,
    val lang_id: String
)
