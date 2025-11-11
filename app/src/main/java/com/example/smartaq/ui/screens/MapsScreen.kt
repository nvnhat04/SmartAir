@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartaq.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartaq.ui.components.LocationCard
import com.example.smartaq.ui.components.getAddressFromLatLon
import com.example.smartaq.ui.navigation.Screen
import com.example.smartaq.viewmodel.ForecastViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
//import com.google.android.libraries.places.api.model.LocalDate
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MapsScreen(
    viewModel: ForecastViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var selectedLocation by remember { mutableStateOf(LatLng(21.0285, 105.8542)) } // Default: Hà Nội
    var hasPermission by remember { mutableStateOf(false) }
//
//    val requestPermissionLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
//            if (hasPermission) {
//                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//                    loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
//                }
//            }
//        }

    // Xin quyền khi khởi động
//    LaunchedEffect(Unit) {
//        if (ActivityCompat.checkSelfPermission(
//                context, Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        } else {
//            hasPermission = true
//            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//                loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
//            }
//        }
//    }


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(
                text = "Chọn vị trí trên bản đồ",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            ) })
        }
//        ,
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
////                // Tìm kiếm địa điểm
////                val fields = listOf(
////                    com.google.android.libraries.places.api.model.Place.Field.ID,
////                    com.google.android.libraries.places.api.model.Place.Field.NAME,
////                    com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
////                )
////                val intent = Autocomplete.IntentBuilder(
////                    AutocompleteActivityMode.OVERLAY, fields
////                ).build(context)
////                (context as ComponentActivity).startActivityForResult(intent, 100)
//            }) {
//                Text("🔍")
//            }
//        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedLocation = latLng
                }
            ) {
                Marker(
                    state = MarkerState(position = selectedLocation),
                    title = "Vị trí đã chọn",
                    snippet = "(${selectedLocation.latitude}, ${selectedLocation.longitude})"
                )
            }

            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Text("Lat: ${selectedLocation.latitude}")
//                Text("Lon: ${selectedLocation.longitude}")
                val lat = selectedLocation.latitude
                val lon = selectedLocation.longitude
                val address = remember(lat, lon) {
                    getAddressFromLatLon(context, lat, lon)
                }
//                Text(text = address ?: "Không xác định địa chỉ")

                val request_date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
//                Log.d("Request Date", )
                LaunchedEffect(lat, lon) {
                    viewModel.fetchForecast(lat, lon, request_date, 3, 3)
                }
                val forecast = viewModel.forecast.collectAsState().value
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
                        val aqi_val = valuesAqi[3].toInt()
                        val pm25_val = valuesPm25[3].toDouble()


                        LocationCard(
                            title = "Vị trí của bạn",
                            address = address.toString(),
                            request_date = request_date,
                            lat = lat,
                            lon = lon,
                            aqi = aqi_val,
                            pm25 = pm25_val,
                            onClick = {
                                navController.navigate(Screen.Advisor.createRoute(lat, lon))
                            }
                        )
                    }
                }

            }
        }
    }
}

