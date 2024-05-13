package com.example.aplicacionmusicatfg.controladores

import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.modelos.Usuario
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsuarioController: ViewModel() {
    val database = Firebase.database
    val myRef = database.getReference("Usuarios")

    fun subirUsuario(usuario: Usuario, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val nuevoUsuarioRef = myRef.push()
        nuevoUsuarioRef.setValue(usuario)
            .addOnSuccessListener {
                val pushKey = nuevoUsuarioRef.key
                if (pushKey != null) {
                    onSuccess(pushKey)
                } else {
                    onError("No se pudo obtener la clave push del usuario recién creado.")
                }

            }
            .addOnFailureListener { exception ->
                onError("Error al subir el usuario: ${exception.message}")
            }
    }
}