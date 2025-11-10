package com.example.smartaq.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.smartaq.ui.screens.AccountScreen
import com.example.smartaq.ui.screens.HomeScreen
import com.example.smartaq.ui.screens.NotificationScreen


@Composable
fun NaviGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ){
//        composable("main") {
//            GetLayout (
//                navController = navController
//            )
//        }
//
//        composable("login") {
//            LoginScreen(
//                viewModel = viewModel(),
//                navController = navController
//            )
//        }
//        composable("register") {
//            RegisterScreen(
//                viewModel = viewModel(),
//                navController = navController
//            )
//        }

        composable("home") {
            HomeScreen(
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
        composable("notification") {
            NotificationScreen(
                navController = navController,
//                viewModel = viewModel()
            )
        }

    }
}