package com.example.aplicacionmusicatfg.controladores

import android.content.ContentValues
import android.util.Log
import com.example.aplicacionmusicatfg.modelos.Genero
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//Esto debe ser una clase viewModel
fun getListaGenerosRealtime(callback: (List<Genero>) -> Unit) {
    val database = Firebase.database
    val myRef = database.getReference("Generos")

    // Read from the database
    myRef.addValueEventListener(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val generos: MutableList<Genero> = mutableListOf()

            for (generoSnapshot in snapshot.children) {

                val nombre = generoSnapshot.child("Nombre").getValue(String::class.java)
                val descripcion = generoSnapshot.child("Descripcion").getValue(String::class.java)
                val imagen = generoSnapshot.child("Imagen").getValue(String::class.java)

                if (nombre != null && descripcion != null && imagen !=null) {

                    val genero = Genero()
                    genero.nombre = nombre
                    genero.descripcion = descripcion
                    genero.imagen = imagen

                    generos.add(genero)
                } else {
                    Log.e(ContentValues.TAG, "Error: Alguno de los valores es nulo para el genero")
                }
            }
            callback(generos)
        }
        override fun onCancelled(error: DatabaseError) {
            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            callback(emptyList())
        }
    })
}