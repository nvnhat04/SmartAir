package com.example.smartaq.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.smartaq.ui.navigation.Screen

// ---------- COLOR + STATUS HELPERS ----------
fun getAqiColor(aqi: Int): Color = when {
    aqi <= 50 -> Color(0xFF00E400)
    aqi <= 100 -> Color(0xFFFFFF00)
    aqi <= 150 -> Color(0xFFFF7E00)
    aqi <= 200 -> Color(0xFFFF0000)
    else -> Color(0xFF8E24AA)
}

fun getAqiStatus(aqi: Int): String = when {
    aqi <= 50 -> "Tốt"
    aqi <= 100 -> "Trung bình"
    aqi <= 150 -> "Không tốt cho nhóm nhạy cảm"
    aqi <= 200 -> "Không tốt"
    else -> "Nguy hiểm"
}

// =====================================================
// ================ LOCATION ITEM =======================
// =====================================================

@Composable
fun LocationItemCard(
    locationName: String,
    address: String,
    currentAqi: Int,
    lat: Double,
    lon: Double,
    forecastDays: List<Pair<String, Int?>>,
    navController: NavController
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth().height(180.dp) // xác định chiều cao
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AQICardLeft(
                modifier = Modifier.fillMaxHeight().weight(1f),
                locationName = locationName,
                address = address,
                aqi = currentAqi,
                lat = lat,
                lon = lon,
                navController = navController
            )

            ForecastCardRight(
                modifier = Modifier.fillMaxHeight().weight(1f),
                forecastDays = forecastDays
            )
        }
    }
}

@Composable
fun AQICardLeft(
    modifier: Modifier = Modifier,
    locationName: String,
    address: String,
    aqi: Int,
    lat: Double,
    lon: Double,
    navController: NavController
) {
    Card(
        modifier = modifier.clickable { navController.navigate(Screen.Advisor.createRoute(lat, lon)) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = getAqiColor(aqi))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(locationName, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(address, fontSize = 12.sp, color = Color.Black.copy(alpha = 0.7f))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$aqi", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(getAqiStatus(aqi), color = Color.Black)
            }
        }
    }
}

@Composable
fun ForecastCardRight(
    modifier: Modifier = Modifier,
    forecastDays: List<Pair<String, Int?>>
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            forecastDays.forEachIndexed { idx, (label, value) ->
                // Mỗi row chiếm đều chiều cao
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),  // chia đều
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, fontWeight = FontWeight.Medium)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(getAqiColor(value ?: 0))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = value?.toString() ?: "-",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (idx < forecastDays.size - 1) {
                    Divider(color = Color.LightGray.copy(alpha = 0.6f))
                }
            }
        }
    }
}