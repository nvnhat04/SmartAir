package com.example.smartaq

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.getValue

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.viewbinding.BuildConfig
import com.example.smartaq.ui.components.BottomNavItem
import com.example.smartaq.ui.components.NaviBar
import com.example.smartaq.ui.navigation.NaviGraph
import kotlin.collections.contains


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route


            // Danh sách route được phép hiện bottom bar
            val bottomBarRoutes = listOf("home", "profile", "notification")
//            val bottomAdminBarRoutes = listOf( "profile", "add-product", "admin",)
            val items = listOf(
                BottomNavItem("Home", Icons.Default.Home, "home"),
                BottomNavItem("Notification", Icons.Default.Notifications, "Notification"),
                BottomNavItem("Profile", Icons.Default.Person, "profile"),



                )
//            val itemsAdmin = listOf(
//                BottomNavItem("Admin", Icons.Default.ManageAccounts, "admin"),
//                BottomNavItem("Profile", Icons.Default.Person, "profile"),
//                BottomNavItem("Add", Icons.Default.Add, "add-product")
//            )

            Scaffold(
                bottomBar = {
                    NaviBar(navController, items)
                        }
            ) {
                Box(modifier = Modifier.padding(it)) {
                    NaviGraph(navController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}




//class MainActivity : ComponentActivity() {
//
//    private lateinit var autocompleteLauncher: ActivityResultLauncher<Intent>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
////        if (!Places.isInitialized()) {
////            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
////        }
//
//        setContent {
//            MapScreen()
//        }
//    }
//}

//    @OptIn(ExperimentalMaterial3Api::class)
//    @Composable
//    fun MapScreen() {
//        val context = LocalContext.current
//        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//        var selectedLocation by remember { mutableStateOf(LatLng(21.0285, 105.8542)) } // Default: Hà Nội
//        var hasPermission by remember { mutableStateOf(false) }
//
//        val requestPermissionLauncher =
//            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//                hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
//                if (hasPermission) {
//                    fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//                        loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
//                    }
//                }
//            }
//
//        // Xin quyền khi khởi động
//        LaunchedEffect(Unit) {
//            if (ActivityCompat.checkSelfPermission(
//                    context, Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestPermissionLauncher.launch(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    )
//                )
//            } else {
//                hasPermission = true
//                fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
//                    loc?.let { selectedLocation = LatLng(it.latitude, it.longitude) }
//                }
//            }
//        }
//
//        val cameraPositionState = rememberCameraPositionState {
//            position = CameraPosition.fromLatLngZoom(selectedLocation, 12f)
//        }
//
//        Scaffold(
//            topBar = {
//                TopAppBar(title = { Text("SmartAQ - Chọn vị trí & tìm kiếm") })
//            },
//            floatingActionButton = {
//                FloatingActionButton(onClick = {
//                    // Tìm kiếm địa điểm
//                    val fields = listOf(
//                        com.google.android.libraries.places.api.model.Place.Field.ID,
//                        com.google.android.libraries.places.api.model.Place.Field.NAME,
//                        com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
//                    )
//                    val intent = Autocomplete.IntentBuilder(
//                        AutocompleteActivityMode.OVERLAY, fields
//                    ).build(context)
//                    (context as ComponentActivity).startActivityForResult(intent, 100)
//                }) {
//                    Text("🔍")
//                }
//            }
//        ) { padding ->
//            Box(Modifier.fillMaxSize().padding(padding)) {
//                GoogleMap(
//                    modifier = Modifier.fillMaxSize(),
//                    cameraPositionState = cameraPositionState,
//                    onMapClick = { latLng ->
//                        selectedLocation = latLng
//                    }
//                ) {
//                    Marker(
//                        state = MarkerState(position = selectedLocation),
//                        title = "Vị trí đã chọn",
//                        snippet = "(${selectedLocation.latitude}, ${selectedLocation.longitude})"
//                    )
//                }
//
//                Column(
//                    Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp)
//                        .fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text("Lat: ${selectedLocation.latitude}")
//                    Text("Lon: ${selectedLocation.longitude}")
//                    Button(onClick = {
//                        // TODO: Gọi API dự báo PM2.5 ở đây
//                    }) {
//                        Text("Dự báo PM2.5 tại đây")
//                    }
//                }
//            }
//        }
//    }
//}
