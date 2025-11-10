package com.example.smartaq.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun NaviBar(navController: NavController, items: List<BottomNavItem>) {
//    val items = listOf(
//        BottomNavItem("Home", Icons.Default.Home, "home"),
//        BottomNavItem("Cart", Icons.Default.ShoppingCart, "cart"),
//        BottomNavItem("Profile", Icons.Default.Person, "profile"),
//        BottomNavItem("CheckOut", Icons.Default.Checklist, "checkout"),
//        BottomNavItem("Add", Icons.Default.Add, "add-product")
//
//    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = false, // Bạn có thể so sánh với currentRoute
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }
    }
}