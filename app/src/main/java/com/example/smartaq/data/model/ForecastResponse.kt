package com.example.smartaq.data.model

import com.google.gson.annotations.SerializedName


data class ForecastResponse(
    val Message: String?,
    val Data: ForecastData?,
    val Error: Any?,
    val Code: Int
)

data class ForecastData(
    val comps: List<ComponentData>
)

data class ComponentData(
    val geotiff: Boolean,
    val gcid: String,
    val requestdate: String,
    val titlegroup: String,
    val titlecomponent: String,
    val x: Double,
    val y: Double,
    @SerializedName("val") val value: Double,   // ✅ dùng @SerializedName nếu tên là val     // Kotlin không cho đặt tên là "val"
    val val_aqi: Double
)
