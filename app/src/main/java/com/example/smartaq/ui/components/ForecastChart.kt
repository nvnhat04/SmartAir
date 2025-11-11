package com.example.smartaq

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartaq.data.model.ComponentData
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Fill

import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.common.data.ExtraStore


import kotlinx.coroutines.launch

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

// Format trục dọc (AQI giá trị thô)
private val StartAxisValueFormatter = CartesianValueFormatter { _, value, _ ->
    value.toInt().toString()
}

// Format trục ngang (hiển thị ngày)
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

// Hàm màu AQI
private fun getAqiColor(value: Float): Color {
    return when {
        value <= 50 -> Color(0xFF00E400)   // Xanh lá - Tốt
        value <= 100 -> Color(0xFFFFFF00)  // Vàng - Trung bình
        value <= 150 -> Color(0xFFFF7E00)  // Cam - Kém
        else -> Color(0xFFFF0000)          // Đỏ - Xấu
    }
}

@Composable
fun ForecastChart(
    valuesAqi: List<Float>,
    valuesDate: List<String>,
    modifier: Modifier = Modifier

) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val scope = rememberCoroutineScope()

//    val valuesAqi = comps.map { it.val_aqi.toFloat() }.asReversed()
//    val valuesDate = comps.map { request ->
//        request.requestdate.toString().takeLast(2) // lấy 2 ký tự cuối (ngày)
//    }.asReversed()


    LaunchedEffect(valuesAqi, valuesDate) {
        scope.launch {
            modelProducer.runTransaction {
                columnSeries { series(valuesAqi.asReversed()) } // đảo để hiển thị gần giống như trước
                extras { it[BottomAxisLabelKey] = valuesDate.asReversed() }
            }
        }
    }

    // ColumnProvider: thay đổi màu từng cột theo AQI
//    val columnProvider = remember(valuesAqi) {
//        ColumnCartesianLayer.ColumnProvider { _, index, _ ->
//            val color = getAqiColor(valuesAqi.getOrNull(index) ?: 0f)
//            ColumnCartesianLayer.Column(
//                lineComponent = rememberLineComponent(
//                    fill = Fill(color),
//                    thickness = 16.dp
//                )
//            )
//        }
//    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(valueFormatter = StartAxisValueFormatter),
            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter,
            ),
        ),
        modelProducer = modelProducer,
        modifier = modifier.height(216.dp),
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}
