package com.example.smartaq.ui.components

import android.location.Geocoder
import java.util.Locale
import android.content.Context

fun getAddressFromLatLon(
    context: Context,
    lat: Double,
    lon: Double
): String? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            address.getAddressLine(0) // trả về địa chỉ đầy đủ
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
