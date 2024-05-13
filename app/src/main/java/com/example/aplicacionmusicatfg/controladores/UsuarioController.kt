package com.example.aplicacionmusicatfg.controladores

import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.modelos.Usuario
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsuarioController: ViewModel() {
    private val database = Firebase.database
    private val myRef = database.getReference("Usuarios")
    private var refKey = ""

    fun subirUsuario(usuario: Usuario, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val nuevoUsuarioRef = myRef.push()

        nuevoUsuarioRef.setValue(usuario)
            .addOnSuccessListener {
                refKey = nuevoUsuarioRef.key.toString()
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError("Error al subir la canción: ${exception.message}")
            }
    }
}