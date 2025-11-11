package com.example.smartaq.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun LocationCard(
    title: String,
    address: String,
    request_date: String,
    lat: Double,
    lon: Double,
    aqi: Int? = null,
    pm25: Double? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location icon",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = request_date, fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = address, fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Lat: ${String.format("%.4f", lat)} | Lon: ${String.format("%.4f", lon)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                val aqiColor = when (aqi!!) {
                    in 0..50 -> Color(0xFF4CAF50)
                    in 51..100 -> Color(0xFFFFC107)
                    in 101..150 -> Color(0xFFFF9800)
                    in 151..200 -> Color(0xFFF44336)
                    else -> Color(0xFF9C27B0)
                }
                if (aqi != null && pm25 != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("PM2.5: ${String.format("%.3f", pm25)}  µg/m³  |  AQI: $aqi", fontSize = 13.sp, color = aqiColor)
                }
            }

            if (onClick != null) {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Details",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}
