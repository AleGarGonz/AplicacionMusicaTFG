package com.example.aplicacionmusicatfg.controladores

import com.example.aplicacionmusicatfg.modelos.ListaReproduccion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListaReproController {
    private val database = Firebase.database
    private val myRef = database.getReference("Listasrepro")

    fun subirListaReproduccion(listaReproduccion: ListaReproduccion,uid:String) {
        // Generar una nueva clave única para la lista de reproducción
        val nuevaListaRef = myRef.child(uid).child("1")

        // Subir la lista de reproducción a la base de datos
        nuevaListaRef.setValue(listaReproduccion)
            .addOnSuccessListener {
                println("La lista de reproducción se subió exitosamente a Firebase.")
            }
            .addOnFailureListener { exception ->
                println("Error al subir la lista de reproducción a Firebase: $exception")
            }
    }

    fun descargarListaReproduccion(uid: String, listaNumero: Int, callback: (ListaReproduccion?) -> Unit) {
        val listaRef = myRef.child(uid).child(listaNumero.toString())

        // Escuchar una vez para obtener la lista de reproducción
        listaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = snapshot.getValue(ListaReproduccion::class.java)
                callback(lista)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
                println("Error al descargar la lista de reproducción: $error")
            }
        })
    }

    fun descargarTodasLasListasReproduccion(uid: String, callback: (List<ListaReproduccion>) -> Unit) {
        val listas: MutableList<ListaReproduccion> = mutableListOf()

        // Obtener la referencia a la ubicación de las listas de reproducción del usuario
        val listaRef = myRef.child(uid)

        // Escuchar los cambios en la referencia de las listas de reproducción
        listaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (listaSnapshot in snapshot.children) {
                    val lista = listaSnapshot.getValue(ListaReproduccion::class.java)
                    lista?.let { listas.add(it) }
                }
                // Llamar al callback con la lista de reproducción
                callback(listas)
            }

            override fun onCancelled(error: DatabaseError) {
                // En caso de error, llamar al callback con una lista vacía
                callback(emptyList())
                println("Error al descargar las listas de reproducción: $error")
            }
        })
    }
}