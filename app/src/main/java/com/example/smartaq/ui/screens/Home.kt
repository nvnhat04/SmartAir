@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartaq.ui.screens

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen( navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var selectedLocation by remember { mutableStateOf(LatLng(21.0285, 105.8542)) } // Default: Hà Nội
    var hasPermission by remember { mutableStateOf(false) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (hasPermission) {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                    loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
                }
            }
        }

    // Xin quyền khi khởi động
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
            TopAppBar(title = { Text("SmartAQ - Chọn vị trí & tìm kiếm") })
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
                Text("Lat: ${selectedLocation.latitude}")
                Text("Lon: ${selectedLocation.longitude}")
                Button(onClick = {
                    // TODO: Gọi API dự báo PM2.5 ở đây
                }) {
                    Text("Dự báo PM2.5 tại đây")
                }
            }
        }
    }
}

