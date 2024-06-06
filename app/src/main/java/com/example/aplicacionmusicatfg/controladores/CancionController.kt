package com.example.aplicacionmusicatfg.controladores

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class CancionController: ViewModel() {
    val storageRef = FirebaseStorage.getInstance().reference
    val database = Firebase.database
    val myRef = database.getReference("Canciones")

///////////////////////////////////////////////////////////////////////////////////////////
//Storage
//Descarga la cancion del Storage en formato MP3
fun getCancionStorage(context: Context, fileName: String, callback: (File?, Exception?) -> Unit) {
    // Crear un archivo local persistente en el directorio de almacenamiento interno de la aplicación
    val localFile = File(context.filesDir, "${fileName}.mp3")
    // Verificar si el archivo ya existe localmente
    if (localFile.exists()) {
        // Llamar al callback con el archivo local
        callback(localFile, null)
    } else {
        // Si el archivo no existe localmente, descargarlo de Firebase Storage
        val audioRef = storageRef.child("audios/${fileName}.mp3")

        audioRef.getFile(localFile)
            .addOnSuccessListener {
                // Llamar al callback con el archivo local
                callback(localFile, null)
            }
            .addOnFailureListener { exception ->
                // Manejar errores de descarga llamando al callback con la excepción
                Log.e("Descarga", "Fallida ${fileName}", exception)
                callback(null, exception)
            }
    }
}


    fun getListaSinConexionCancionStorage(context: Context, canciones: List<Cancion>, callback: (List<File>?, Exception?) -> Unit) {
        val archivosDescargados = mutableListOf<File>()

        canciones.forEach { cancion ->
            // Crear un archivo local persistente en el directorio de almacenamiento interno de la aplicación
            val localFile = File(context.filesDir, "${cancion.audio}.mp3")

            // Verificar si el archivo ya existe localmente
            if (localFile.exists()) {
                archivosDescargados.add(localFile)
            } else {
                // Si el archivo no existe localmente, descargarlo de Firebase Storage
                val audioRef = storageRef.child("audios/${cancion.audio}.mp3")

                audioRef.getFile(localFile)
                    .addOnSuccessListener {
                        // Agregar el archivo descargado a la lista
                        archivosDescargados.add(localFile)

                        // Verificar si todas las canciones han sido descargadas
                        if (archivosDescargados.size == canciones.size) {
                            // Llamar al callback con la lista de archivos descargados
                            callback(archivosDescargados, null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Manejar errores de descarga llamando al callback con la excepción
                        Log.e("Descarga", "Fallida ${cancion.audio}", exception)

                        // Verificar si todas las canciones han sido descargadas
                        if (archivosDescargados.size == canciones.size) {
                            // Llamar al callback con la lista de archivos descargados hasta ahora
                            callback(archivosDescargados, exception)
                        }
                    }
            }
        }

        // Si todas las canciones ya existen localmente, llamar al callback con la lista de archivos
        if (archivosDescargados.size == canciones.size) {
            callback(archivosDescargados, null)
        }
    }





///////////////////////////////////////////////////////////////////////////////////////////
//Realtime Database
fun buscarCancionesPorTitulo(titulo: String, callback: (List<Cancion>) -> Unit) {
    myRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val canciones: MutableList<Cancion> = mutableListOf()
            for (cancionSnapshot in snapshot.children) {
                val artista = cancionSnapshot.child("Artista").getValue(String::class.java)
                val audio = cancionSnapshot.child("Audio").getValue(String::class.java)
                val genero = cancionSnapshot.child("Genero").getValue(String::class.java)
                val imagen = cancionSnapshot.child("Imagen").getValue(String::class.java)
                val tituloDB = cancionSnapshot.child("Titulo").getValue(String::class.java)

                if (artista != null && audio != null && genero != null && imagen != null && tituloDB != null) {
                    val tituloLowerCase = tituloDB.toLowerCase()
                    if (tituloLowerCase.contains(titulo.toLowerCase())) {
                        val cancion = Cancion()
                        cancion.artista = artista
                        cancion.audio = audio
                        cancion.genero = genero
                        cancion.imagen = imagen
                        cancion.titulo = tituloDB // Mantener el título original sin cambios
                        canciones.add(cancion)
                    }
                } else {
                    Log.e(ContentValues.TAG, "Error: Alguno de los valores es nulo para la canción")
                }
            }
            callback(canciones)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            callback(emptyList())
        }
    })
}


    fun buscarCancionesPorArtista(artista: String, callback: (List<Cancion>) -> Unit) {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val canciones: MutableList<Cancion> = mutableListOf()
                for (cancionSnapshot in snapshot.children) {
                    val artistaDB = cancionSnapshot.child("Artista").getValue(String::class.java)
                    val audio = cancionSnapshot.child("Audio").getValue(String::class.java)
                    val genero = cancionSnapshot.child("Genero").getValue(String::class.java)
                    val imagen = cancionSnapshot.child("Imagen").getValue(String::class.java)
                    val titulo = cancionSnapshot.child("Titulo").getValue(String::class.java)

                    if (artistaDB != null && audio != null && genero != null && imagen != null && titulo != null) {
                        val artistaLowerCase = artistaDB.toLowerCase()
                        if (artistaLowerCase.contains(artista.toLowerCase())) {
                            val cancion = Cancion()
                            cancion.artista = artistaDB // Mantener el nombre del artista original sin cambios
                            cancion.audio = audio
                            cancion.genero = genero
                            cancion.imagen = imagen
                            cancion.titulo = titulo
                            canciones.add(cancion)
                        }
                    } else {
                        Log.e(ContentValues.TAG, "Error: Alguno de los valores es nulo para la canción")
                    }
                }
                callback(canciones)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                callback(emptyList())
            }
        })
    }
}