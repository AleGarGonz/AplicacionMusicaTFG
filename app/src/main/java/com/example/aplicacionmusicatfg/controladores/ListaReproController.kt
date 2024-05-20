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

    fun subirListaReproduccion(listaReproduccion: ListaReproduccion,uid:String,idlista:String) {
        // Generar una nueva clave única para la lista de reproducción
        val nuevaListaRef = myRef.child(uid).child(idlista)

        // Subir la lista de reproducción a la base de datos
        nuevaListaRef.setValue(listaReproduccion)
            .addOnSuccessListener {
                println("La lista de reproducción se subió exitosamente a Firebase.")
            }
            .addOnFailureListener { exception ->
                println("Error al subir la lista de reproducción a Firebase: $exception")
            }
    }
    fun actualizarListaReproduccion(listaReproduccion: ListaReproduccion, uid: String, idLista: String) {
        // Crear un mapa para almacenar los campos que se van a actualizar
        val actualizacionMap = mutableMapOf<String, Any?>()
        actualizacionMap["Listanombre"] = listaReproduccion.Listanombre
        actualizacionMap["Canciones"] = listaReproduccion.Canciones

        // Referencia a la lista de reproducción a actualizar
        val listaRef = myRef.child(uid).child(idLista)

        // Actualizar la lista de reproducción en la base de datos
        listaRef.updateChildren(actualizacionMap)
            .addOnSuccessListener {
                println("La lista de reproducción se actualizó exitosamente en Firebase.")
            }
            .addOnFailureListener { exception ->
                println("Error al actualizar la lista de reproducción en Firebase: $exception")
            }
    }

    fun descargarListaReproduccion(uid: String, listaId: String, callback: (ListaReproduccion?) -> Unit) {
        val listaRef = myRef.child(uid).child(listaId)

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

    fun borrarListaReproduccion(uid: String, listaId: String) {
        // Referencia a la lista de reproducción que se va a borrar
        val listaRef = myRef.child(uid).child(listaId)

        // Borrar la lista de reproducción de la base de datos
        listaRef.removeValue()
            .addOnSuccessListener {
                println("La lista de reproducción se borró exitosamente de Firebase.")
            }
            .addOnFailureListener { exception ->
                println("Error al borrar la lista de reproducción de Firebase: $exception")
            }
    }
}