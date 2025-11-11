package com.example.smartaq.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    val context = LocalContext.current

    // Giả lập dữ liệu user — bạn có thể thay bằng ViewModel hoặc API sau này
    val userName = "Smart Air"
    val userEmail = "user@smartair.com"
    val userId = "UID12345"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tài khoản") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(90.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tên user
            Text(
                text = userName,
//                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FnotWeight.Bold)
            )

            // Email
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Thông tin thêm
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Mã người dùng: $userId", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Vai trò: Người dùng", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Trạng thái: Đang hoạt động", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Nút Sign out
            Button(
                onClick = {
                    // TODO: Xử lý đăng xuất (xóa token, navigate về màn hình login)
                    Toast.makeText(context, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
                    navController.navigate("login") {
                        popUpTo(0) // Xóa toàn bộ backstack
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Đăng xuất", color = Color.White)
            }
        }
    }
}
