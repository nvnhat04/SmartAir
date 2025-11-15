@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartaq.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.smartaq.ui.components.LocationCard
import com.example.smartaq.ui.components.getAddressFromLatLon
import com.example.smartaq.ui.navigation.Screen
import com.example.smartaq.viewmodel.ForecastViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ForecastViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var selectedLocation by remember { mutableStateOf(LatLng(21.0285, 105.8542)) }
    var hasPermission by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (hasPermission) {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
                }
            }
        }

    // Xin quyền
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            hasPermission = true
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 12f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Logo + tên app
                        Text(
                            text = "SmartAir",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Icon search -> mở màn hình tìm kiếm
//                            IconButton(onClick = {
//                                navController.navigate("search")
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Default.Search,
//                                    contentDescription = "Search",
//                                    tint = MaterialTheme.colorScheme.onSurface
//                                )
//                            }

                            // Icon notification
                            IconButton(onClick = {
                                navController.navigate("notifications")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Icon profile
                            IconButton(onClick = {
                                navController.navigate("profile")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            )
        }

    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val lat = selectedLocation.latitude
                val lon = selectedLocation.longitude
                val address = remember(lat, lon) {
                    getAddressFromLatLon(context, lat, lon)
                }
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

                Button(onClick = {
                    navController.navigate("weather")
                }) {
                    Text("Thêm địa điểm")
                }
            }
        }
    }
}
