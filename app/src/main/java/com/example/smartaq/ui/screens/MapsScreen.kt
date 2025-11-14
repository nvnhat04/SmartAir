package com.example.smartaq.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.smartaq.R
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartaq.ui.components.LocationCard
import com.example.smartaq.ui.components.getAddressFromLatLon
import com.example.smartaq.ui.navigation.Screen
import com.example.smartaq.viewmodel.ForecastViewModel
import com.google.android.gms.location.LocationServices

import com.google.maps.android.compose.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    viewModel: ForecastViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    var selectedLocation by remember { mutableStateOf(LatLng(21.0285, 105.8542)) } // Hà Nội default
    var destinationLocation by remember { mutableStateOf<LatLng?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 12f)
    }

    // Lấy vị trí hiện tại nếu có quyền
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column(

                ) {
                    Text("Tìm kiếm vị trí", style = MaterialTheme.typography.titleLarge)
                    // Ô tìm kiếm địa điểm
//                    OutlinedTextField(
//                        value = searchQuery,
//                        onValueChange = { searchQuery = it },
//                        modifier = Modifier.fillMaxWidth(),
//                        placeholder = { Text("Nhập địa điểm cần tìm...") },
//                        trailingIcon = {
//                            val coroutineScope = rememberCoroutineScope()
//
//                            IconButton(onClick = {
//                                if (searchQuery.isNotEmpty()) {
//                                    coroutineScope.launch {
//                                        searchPlace(context, searchQuery) { latLng ->
//                                            destinationLocation = latLng
//                                            cameraPositionState.position =
//                                                CameraPosition.fromLatLngZoom(latLng, 15f)
//                                        }
//                                    }
//                                }
//                            }) {
//                                Icon(Icons.Default.Search, contentDescription = "Search")
//                            }
//
//                        }
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Nút thêm vào Home
//                    Button(
//                        onClick = {
//                            destinationLocation?.let { dest ->
////                            viewModel.saveLocation(dest.latitude, dest.longitude)
//                                Toast.makeText(context, "Đã thêm vào Home!", Toast.LENGTH_SHORT).show()
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("⭐ Thêm vào Home")
//                    }


                }

            })
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng -> selectedLocation = latLng }
            ) {
                // Marker vị trí hiện tại
                Marker(state = MarkerState(selectedLocation), title = "Vị trí của bạn")
                // Marker điểm đến
                destinationLocation?.let { dest ->
                    Marker(state = MarkerState(dest), title = "Điểm đến")
                    DrawRoute(context, selectedLocation, dest)
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
//                // Ô tìm kiếm địa điểm
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Nhập địa điểm cần tìm...") },
                    trailingIcon = {
                        val coroutineScope = rememberCoroutineScope()

                        IconButton(onClick = {
                            if (searchQuery.isNotEmpty()) {
                                coroutineScope.launch {
                                    searchPlace(context, searchQuery) { latLng ->
                                        destinationLocation = latLng
                                        cameraPositionState.position =
                                            CameraPosition.fromLatLngZoom(latLng, 15f)
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }

                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nút thêm vào Home
                Button(
                    onClick = {
                        destinationLocation?.let { dest ->
//                            viewModel.saveLocation(dest.latitude, dest.longitude)
                            Toast.makeText(context, "Đã thêm vào Home!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("⭐ Thêm vào Home")
                }
            }



            // Card hiển thị dữ liệu AQI/PM2.5
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val lat = selectedLocation.latitude
                val lon = selectedLocation.longitude
                val address = remember(lat, lon) { getAddressFromLatLon(context, lat, lon) }
//                Log.d("Adress", address.toString())
                val request_date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

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
                    if (comps.isNotEmpty()) {
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

// --------------------------
// Hàm tìm kiếm địa điểm
suspend fun searchPlace(context: Context, query: String, onResult: (LatLng) -> Unit) {
    val apiKey = context.getString(R.string.google_maps_key)
    val url = "https://maps.googleapis.com/maps/api/geocode/json" +
            "?address=${URLEncoder.encode(query, "UTF-8")}&key=$apiKey"

    withContext(Dispatchers.IO) {
        val response = URL(url).readText()
        Log.d("MapsScreen", "Response: $response")
        val json = JSONObject(response)
        val results = json.getJSONArray("results")
        if (results.length() > 0) {
            val location = results.getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location")
            val lat = location.getDouble("lat")
            val lng = location.getDouble("lng")
            withContext(Dispatchers.Main) {
                onResult(LatLng(lat, lng))
            }
        }
    }
}

// --------------------------
// Hàm vẽ đường đi giữa hai điểm
@Composable
fun DrawRoute(context: Context, origin: LatLng, destination: LatLng) {
    val polyline = remember { mutableStateListOf<LatLng>() }

    LaunchedEffect(destination) {
        val apiKey = context.getString(R.string.google_maps_key)
        val url = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${destination.latitude},${destination.longitude}" +
                "&key=$apiKey"

        withContext(Dispatchers.IO) {
            val response = URL(url).readText()
            val json = JSONObject(response)
            val routes = json.getJSONArray("routes")
            if (routes.length() > 0) {
                val points = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")
                val decodedPath = PolyUtil.decode(points)
                withContext(Dispatchers.Main) {
                    polyline.clear()
                    polyline.addAll(decodedPath)
                }
            }
        }
    }

    Polyline(points = polyline)
}

