package com.example.aplicacionmusicatfg.controladores

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.modelos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsuarioController: ViewModel() {
    private val database = Firebase.database
    private val myRef = database.getReference("Usuarios")

    fun subirUsuario(usuario: Usuario, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val nuevoUsuarioRef = myRef.push()

        nuevoUsuarioRef.setValue(usuario)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError("Error al subir la canción: ${exception.message}")
            }
    }

    fun descargarUsuario(emailBuscado: String, callback: (Usuario?) -> Unit) {
        val database = Firebase.database
        val usuariosRef = database.getReference("Usuarios")

        // Leer de la base de datos
        usuariosRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var usuarioEncontrado: Usuario? = null

                for (usuarioSnapshot in snapshot.children) {
                    val usuario = usuarioSnapshot.getValue(Usuario::class.java)
                    if (usuario?.email == emailBuscado) {
                        usuarioEncontrado = usuario
                        break
                    }
                }

                callback(usuarioEncontrado)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error al leer el valor.", error.toException())
                callback(null)
            }
        })
    }
}