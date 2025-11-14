package com.example.smartaq.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        onError("Không thể lấy UID người dùng")
                        return@addOnCompleteListener
                    }

                    val user = hashMapOf(
                        "uid" to uid,
                        "name" to name,
                        "email" to email,
                        "role" to "user",
                        "createdAt" to System.currentTimeMillis()
                    )

                    Log.d("Register", "Tạo tài khoản thành công: $uid")

                    db.collection("users").document(uid).set(user)
                        .addOnSuccessListener {
                            Log.d("Register", "Đã lưu thông tin người dùng vào Firestore")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Register", "Lỗi ghi Firestore: ${e.message}", e)
                            onError(e.message ?: "Lỗi khi lưu dữ liệu")
                        }

                } else {
                    Log.e("Register", "Đăng ký thất bại: ${task.exception?.message}", task.exception)
                    onError(task.exception?.message ?: "Đăng ký thất bại")
                }
            }
    }


    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { document ->
                            val role = document.getString("role") ?: "user"
                            onSuccess(role)
                        }
                        .addOnFailureListener {
                            onError("Lỗi lấy dữ liệu role")
                        }
                } else {
                    onError(task.exception?.message ?: "Đăng nhập thất bại")
                }
            }
    }
    fun signOut(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}
