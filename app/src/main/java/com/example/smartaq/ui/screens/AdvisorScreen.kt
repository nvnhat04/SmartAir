package com.example.smartaq.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartaq.ForecastChart
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.example.smartaq.data.model.ComponentData
import com.example.smartaq.ui.components.LocationCard
import com.example.smartaq.ui.components.getAddressFromLatLon
import com.example.smartaq.ui.navigation.Screen
import com.example.smartaq.viewmodel.ForecastViewModel
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.patrykandpatrick.vico.compose.cartesian.*
import com.patrykandpatrick.vico.core.cartesian.axis.*
import com.patrykandpatrick.vico.core.cartesian.data.*
import com.patrykandpatrick.vico.core.cartesian.layer.*
import com.patrykandpatrick.vico.core.common.component.*
import com.patrykandpatrick.vico.core.common.*
import kotlinx.coroutines.launch
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
// Hàm trả về màu theo AQI
fun aqiColor(aqi: Float): Color = when {
    aqi <= 50f -> Color(0xFF4CAF50) // Green
    aqi <= 100f -> Color(0xFFFFC107) // Yellow
    else -> Color(0xFFF44336) // Red
}

@Composable
fun AdvisorScreen(
    viewModel: ForecastViewModel = viewModel(),
    navController: NavController,
    lat: Double,
    lon: Double
) {
    val context = LocalContext.current
    val forecast = viewModel.forecast.collectAsState().value
    val scope = rememberCoroutineScope()

    val request_date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    LaunchedEffect(lat, lon) {
        viewModel.fetchForecast(lat, lon, request_date, 4, 4)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        val address = remember(lat, lon) {
            getAddressFromLatLon(context, lat, lon)
        }
        Text("📅 Dự báo tại ${address} có tọa độ Lat: ${String.format("%.4f", lat)} | Lon: ${String.format("%.4f", lon)} ngày ${request_date}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (forecast == null) {
            Text("Đang tải dữ liệu...")
        } else if (forecast.Code != 200) {
            Text("Lỗi tải dữ liệu!")
        } else {
            val comps = forecast.Data?.comps ?: emptyList()
            if (comps.isEmpty()) {
                Text("Không có dữ liệu")
            } else {
                val valuesAqi = comps.map { it.val_aqi.toFloat() }
                val valuesPm25 = comps.map { it.value.toFloat() }
                val valuesDate = comps.map { request ->
                    request.requestdate.toString().takeLast(2) // lấy 2 ký tự cuối (ngày)
                }.asReversed()
//                val columnLayerAqi = rememberColumnCartesianLayer(
//                    column = { value, _, _ ->
//                        when {
//                            value < 50f -> Color.Green
//                            value < 100f -> Color.Yellow
//                            else -> Color.Red
//                        }
//                    }
//                )
                // ===== Chart AQI =====
//                val modelProducerAqi = remember { CartesianChartModelProducer() }
//                val columnLayerAqi = rememberColumnCartesianLayer()
//                val bottomAxis = rememberBottom(
//                    valueFormatter = { value, _ ->
//                        val index = value.toInt().coerceIn(valuesDate.indices)
//                        valuesDate[index].takeLast(2) // ví dụ "13", "17", ...
//                    }
//                )

//                LaunchedEffect(comps) {
//                    scope.launch {
//                        modelProducerAqi.runTransaction {
//                            columnSeries { series(valuesAqi) }
//                        }
//                    }
//                }

                Text("AQI")
                ForecastChart(valuesAqi,valuesDate)
//                CartesianChartHost(
//                    chart = rememberCartesianChart(
//                        columnLayerAqi,
//                        startAxis = VerticalAxis.rememberStart(),
//                        bottomAxis = HorizontalAxis.rememberBottom()
//                    ),
//                    modelProducer = modelProducerAqi,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                )

                Spacer(modifier = Modifier.height(32.dp))

//                // ===== Chart PM2.5 =====
//                val modelProducerPm25 = remember { CartesianChartModelProducer() }
//                val columnLayerPm25 = rememberColumnCartesianLayer()
//
//                LaunchedEffect(comps) {
//                    scope.launch {
//                        modelProducerPm25.runTransaction {
//                            columnSeries { series(valuesPm25) }
//                        }
//                    }
//                }

                Text("PM2.5 (µg/m³)")
                ForecastChart(valuesPm25,valuesDate)
//                CartesianChartHost(
//                    chart = rememberCartesianChart(
//                        columnLayerPm25,
//                        startAxis = VerticalAxis.rememberStart(),
//                        bottomAxis = HorizontalAxis.rememberBottom()
//                    ),
//                    modelProducer = modelProducerPm25,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                )


            }
        }
    }
}
