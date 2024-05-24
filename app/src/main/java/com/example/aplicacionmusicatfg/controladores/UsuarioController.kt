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
        myRef.orderByChild("email").equalTo(emailBuscado).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuarioEncontrado = snapshot.children.firstOrNull()?.getValue(Usuario::class.java)
                callback(usuarioEncontrado)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error al leer el valor.", error.toException())
                callback(null)
            }
        })
    }

    fun actualizarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        val usuarioQuery = myRef.orderByChild("email").equalTo(usuario.email)
        usuarioQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (usuarioSnapshot in snapshot.children) {
                        usuarioSnapshot.ref.setValue(usuario)
                    }
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Error al leer el valor.", error.toException())
                callback(false)
            }
        })
    }
}