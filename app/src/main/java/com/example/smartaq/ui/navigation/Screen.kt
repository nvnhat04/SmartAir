package com.example.smartaq.ui.navigation

sealed class Screen(val route: String) {
    object Map : Screen("map")
    object Advisor : Screen("advisor?lat={lat}&lon={lon}") {
        fun createRoute(lat: Double, lon: Double) = "advisor?lat=$lat&lon=$lon"
    }
}
