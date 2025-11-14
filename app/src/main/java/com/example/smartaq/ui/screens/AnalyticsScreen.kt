package com.example.smartaq.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartaq.ForecastChart
import com.example.smartaq.viewmodel.ForecastViewModel
import com.example.smartaq.data.model.ForecastResponse
import com.google.android.gms.maps.model.LatLng
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import java.text.SimpleDateFormat
import java.util.*



// -------------------------
// AnalyticsScreen
@Composable
fun AnalyticsScreen(viewModel: ForecastViewModel = viewModel()) {

    val tabTitles = listOf("Tuần", "Tháng", "So sánh")
    var selectedTab by remember { mutableStateOf(0) }

    val homeLocation = LatLng(21.0285, 105.8542)
    val workLocation = LatLng(21.0300, 105.8500)
    val requestDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Fetch dữ liệu cho demo
    LaunchedEffect(Unit) {
        viewModel.fetchForecast(homeLocation.latitude, homeLocation.longitude, requestDate, 3, 3)
    }

    val homeForecast by viewModel.forecast.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchForecast(workLocation.latitude, workLocation.longitude, requestDate, 3, 3)
    }
    val workForecast by viewModel.forecast.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Tabs
        TabRow(selectedTabIndex = selectedTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> WeekChart(homeForecast, workForecast)
            1 -> MonthChart(homeForecast, workForecast)
            2 -> CompareChart(homeForecast, workForecast)
        }
    }
}

// -------------------------
// Chart Tuần
@Composable
fun WeekChart(homeForecast: ForecastResponse?, workForecast: ForecastResponse?) {
    if (homeForecast == null || workForecast == null) {
        Text("Đang tải dữ liệu...")
        return
    }

    val homeAqi = homeForecast.Data?.comps?.map { it.val_aqi.toFloat() } ?: emptyList()
    val workAqi = workForecast.Data?.comps?.map { it.val_aqi.toFloat() } ?: emptyList()
    val dates = homeForecast.Data?.comps?.map { it.requestdate.takeLast(2) } ?: emptyList()

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("AQI trung bình 7 ngày", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("🏠 Nhà")
                ForecastChart(homeAqi, dates)

                Spacer(modifier = Modifier.height(16.dp))
                Text("💼 Nơi làm việc")
                ForecastChart(workAqi, dates)
            }
        }
    }
}

// -------------------------
// Chart Tháng (demo dùng 7 ngày)
@Composable
fun MonthChart(homeForecast: ForecastResponse?, workForecast: ForecastResponse?) {
    if (homeForecast == null || workForecast == null) {
        Text("Đang tải dữ liệu...")
        return
    }

    val homeAqi = homeForecast.Data?.comps?.map { it.val_aqi.toFloat() } ?: emptyList()
    val workAqi = workForecast.Data?.comps?.map { it.val_aqi.toFloat() } ?: emptyList()
    val dates = homeForecast.Data?.comps?.map { it.requestdate.takeLast(2) } ?: emptyList()

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("AQI trung bình 30 ngày", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("🏠 Nhà")
                ForecastChart(homeAqi, dates)

                Spacer(modifier = Modifier.height(16.dp))
                Text("💼 Nơi làm việc")
                ForecastChart(workAqi, dates)
            }
        }
    }
}

// -------------------------
// Chart So sánh (BarChart AQI trung bình)
@Composable
fun CompareChart(homeForecast: ForecastResponse?, workForecast: ForecastResponse?) {
    if (homeForecast == null || workForecast == null) {
        Text("Đang tải dữ liệu...")
        return
    }

    val homeAvg = homeForecast.Data?.comps?.map { it.val_aqi.toFloat() }?.average()?.toFloat() ?: 0f
    val workAvg = workForecast.Data?.comps?.map { it.val_aqi.toFloat() }?.average()?.toFloat() ?: 0f

    val entries = listOf(homeAvg, workAvg)
    val labels = listOf("🏠 Nhà", "💼 Nơi làm việc")

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("So sánh AQI trung bình", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                entries.forEachIndexed { index, value ->
                    Text("${labels[index]}: ${value.toInt()} (${when {
                        value <= 50f -> "🟢 Tốt"
                        value <= 100f -> "🟡 Trung bình"
                        else -> "🔴 Xấu"
                    }})")
                }
            }
        }
    }
}
