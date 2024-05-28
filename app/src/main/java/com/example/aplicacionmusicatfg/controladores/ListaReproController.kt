package com.example.aplicacionmusicatfg.controladores

import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.modelos.ListaReproduccion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListaReproController: ViewModel() {
    private val database = Firebase.database
    private val myRef = database.getReference("Listasrepro")

    fun subirListaReproduccion(listaReproduccion: ListaReproduccion,uid:String,idlista:String) {
        val nuevaListaRef = myRef.child(uid).child(idlista)
        nuevaListaRef.setValue(listaReproduccion)
            .addOnSuccessListener {
                println("La lista de reproducción se subió exitosamente a Firebase.")
            }
            .addOnFailureListener { exception ->
                println("Error al subir la lista de reproducción a Firebase: $exception")
            }
    }
    fun actualizarListaReproduccion(listaReproduccion: ListaReproduccion, uid: String, idLista: String) {
        val actualizacionMap = mutableMapOf<String, Any?>()
        actualizacionMap["canciones"] = listaReproduccion.Canciones

        val listaRef = myRef.child(uid).child(idLista)

        listaRef.updateChildren(actualizacionMap)
            .addOnSuccessListener {
                println("La lista de reproducción se actualizó exitosamente en Firebase.")
            }
            .addOnFailureListener { exception ->
                println("Error al actualizar la lista de reproducción en Firebase: $exception")
            }
    }
    fun descargarListaReproduccion(listaId: String, callback: (ListaReproduccion?) -> Unit) {
        val usersRef = myRef

        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    val listaRef = userSnapshot.child(listaId)
                    if (listaRef.exists()) {
                        val lista = listaRef.getValue(ListaReproduccion::class.java)
                        callback(lista)
                        return
                    }
                }

                callback(null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
                println("Error al descargar la lista de reproducción: $error")
            }
        })
    }
    fun comprobarExistenciaListaReproduccion(uid: String, listaId: String, callback: (Boolean) -> Unit) {
        val userRef = myRef.child(uid)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var existe = false
                for (listaSnapshot in snapshot.children) {
                    if (listaSnapshot.key == listaId) {
                        existe = true
                        break
                    }
                }
                callback(existe)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
                println("Error al comprobar la existencia de la lista de reproducción: $error")
            }
        })
    }

    fun descargarTodasLasListasReproduccion(uid: String, callback: (List<ListaReproduccion>) -> Unit) {
        val listas: MutableList<ListaReproduccion> = mutableListOf()

        val listaRef = myRef.child(uid)

        listaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (listaSnapshot in snapshot.children) {
                    val lista = listaSnapshot.getValue(ListaReproduccion::class.java)
                    lista?.let { listas.add(it) }
                }
                callback(listas)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
                println("Error al descargar las listas de reproducción: $error")
            }
        })
    }
    fun descargarTodasLasListasReproduccionDeTodosLosUsuarios(callback: (List<ListaReproduccion>) -> Unit) {
        val listas: MutableList<ListaReproduccion> = mutableListOf()

        val listaRef = myRef

        listaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    for (listaSnapshot in userSnapshot.children) {
                        val lista = listaSnapshot.getValue(ListaReproduccion::class.java)
                        lista?.let { listas.add(it) }
                    }
                }
                callback(listas)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
                println("Error al descargar las listas de reproducción: $error")
            }
        })
    }

    fun borrarListaReproduccion(uid: String, listaId: String) {
        val listaRef = myRef.child(uid).child(listaId)

        listaRef.removeValue()
            .addOnSuccessListener {
                println("La lista de reproducción se borró exitosamente de Firebase.")
            }
            .addOnFailureListener { exception ->
                println("Error al borrar la lista de reproducción de Firebase: $exception")
            }
    }
}