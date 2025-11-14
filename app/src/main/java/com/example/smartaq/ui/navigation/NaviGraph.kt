package com.example.smartaq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartaq.ui.screens.AccountScreen
import com.example.smartaq.ui.screens.AdvisorScreen
import com.example.smartaq.ui.screens.AnalyticsScreen
import com.example.smartaq.ui.screens.HomeScreen
import com.example.smartaq.ui.screens.IntroScreen
import com.example.smartaq.ui.screens.LoginScreen
import com.example.smartaq.ui.screens.MapsScreen
import com.example.smartaq.ui.screens.NotificationScreen
import com.example.smartaq.ui.screens.RegisterScreen
import com.example.smartaq.ui.screens.SearchScreen


@Composable
fun NaviGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable("intro") {
            IntroScreen (
                navController = navController
            )
        }

        composable("login") {
            LoginScreen(
                viewModel = viewModel(),
                navController = navController
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = viewModel(),
                navController = navController
            )
        }

        composable("home") {
            HomeScreen(
//                viewModel = viewModel(),
                navController = navController
            )
        }
        composable("maps") {
            MapsScreen(
//                viewModel = viewModel(),
                navController = navController
            )
        }
        composable("search") {
            SearchScreen(
//                viewModel = viewModel(),
                navController = navController
            )
        }
        composable("profile") {
            AccountScreen(
                navController = navController,
//                viewModel = viewModel()
            )
        }
        composable("notifications") {
            NotificationScreen(
                navController = navController,
//                viewModel = viewModel()
            )
        }
        composable("analytics") {
            AnalyticsScreen(
                 viewModel = viewModel(),

             )
        }
        composable(
            route = "advisor?lat={lat}&lon={lon}",
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType; defaultValue = 21.0f },
                navArgument("lon") { type = NavType.FloatType; defaultValue = 105.0f }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 21.0
            val lon = backStackEntry.arguments?.getFloat("lon")?.toDouble() ?: 105.0
            AdvisorScreen(viewModel = viewModel(), navController = navController, lat = lat, lon = lon)
        }

    }
}