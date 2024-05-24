package com.example.aplicacionmusicatfg.controladores

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginController: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val loading = MutableLiveData(false)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("SignIn", "signInWithEmailAndPassword: Logueado")
                        onSuccess()
                    } else {
                        Log.d("SignIn", "signInWithEmailAndPassword: Error en el inicio de sesión")
                        onError("Error en el inicio de sesión")
                    }
                }
                .addOnFailureListener { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Email o contraseña incorrectos"
                        is FirebaseAuthInvalidUserException -> "Email o contraseña incorrectos"
                        is FirebaseNetworkException -> "No hay conexión de red"
                        else -> "Ha ocurrido un error"
                    }
                    Log.d("SignIn", "signInWithEmailAndPassword: $errorMessage")
                    onError(errorMessage)
                }
        } catch (ex: Exception) {
            Log.d("SignIn", "signInWithEmailAndPassword: ${ex.message}")
            onError("Ha ocurrido un error")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (loading.value == false) {
            loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError("Error al crear el usuario")
                    }
                    loading.value = false
                }
                .addOnFailureListener { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "La contraseña es débil"
                        is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas"
                        is FirebaseAuthUserCollisionException -> "El usuario ya existe"
                        is FirebaseNetworkException -> "No hay conexión de red"
                        else -> "Ha ocurrido un error"
                    }
                    onError(errorMessage)
                    loading.value = false
                }
        }
    }
    fun resetPasswordByEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                val errorMessage = when (exception) {
                    is FirebaseAuthInvalidUserException -> "No existe ningún usuario con este correo electrónico"
                    is FirebaseNetworkException -> "No hay conexión de red"
                    else -> "Ha ocurrido un error"
                }
                onError(errorMessage)
            }
    }
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    fun signOut() {
        auth.signOut()
    }
    fun verificarEmailUsuario(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            user.sendEmailVerification()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseNetworkException -> "No hay conexión de red"
                        else -> "Ha ocurrido un error al enviar el correo de verificación"
                    }
                    onError(errorMessage)
                }
        } ?: run {
            onError("No hay usuario actualmente logueado")
        }
    }
}