package com.example.aplicacionmusicatfg.controladores

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.File

//Esto debe ser una clase viewModel

fun getImagenStorage(context: Context, fileName: String, callback: (File?, Exception?) -> Unit) {
    // Crear un archivo local persistente en el directorio de almacenamiento interno de la aplicación
    val localFile = File(context.filesDir, "${fileName}")
    // Verificar si el archivo ya existe localmente
    if (localFile.exists()) {
        // Llamar al callback con el archivo local
        callback(localFile, null)
    } else {
        // Si el archivo no existe localmente, descargarlo de Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference
        val imgRef = storageRef.child("imagenes/${fileName}")

        imgRef.getFile(localFile)
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

fun subirImagenStorage(file: File) {
    // Obtener la referencia al Firebase Storage
    val storageRef = FirebaseStorage.getInstance().reference
    // Crear una referencia al archivo en el Storage con un nombre único
    val imgRef = storageRef.child("imagenes/${file.name}")

    // Subir el archivo al Storage
    imgRef.putFile(Uri.fromFile(file))
        .addOnSuccessListener {
            // La subida del archivo fue exitosa
        }
        .addOnFailureListener { exception ->
            // Manejar errores de subida del archivo
        }
}


/*fun getListaImagenGenerosStorage(context: Context, generos: List<Genero>, callback: (List<File>?, Exception?) -> Unit) {
    val archivosDescargados = mutableListOf<File>()

    generos.forEach { genero ->
        // Crear un archivo local persistente en el directorio de almacenamiento interno de la aplicación
        val localFile = File(context.filesDir, "${genero.imagen}.png")

        // Verificar si el archivo ya existe localmente
        if (localFile.exists()) {
            archivosDescargados.add(localFile)
        } else {
            // Si el archivo no existe localmente, descargarlo de Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference
            val imgRef = storageRef.child("imagenes/${genero.imagen}.png")

            imgRef.getFile(localFile)
                .addOnSuccessListener {
                    // Agregar el archivo descargado a la lista
                    archivosDescargados.add(localFile)

                    // Verificar si todas las canciones han sido descargadas
                    if (archivosDescargados.size == generos.size) {
                        // Llamar al callback con la lista de archivos descargados
                        callback(archivosDescargados, null)
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar errores de descarga llamando al callback con la excepción
                    Log.e("Descarga", "Fallida ${genero.imagen}", exception)

                    // Verificar si todas las canciones han sido descargadas
                    if (archivosDescargados.size == generos.size) {
                        // Llamar al callback con la lista de archivos descargados hasta ahora
                        callback(archivosDescargados, exception)
                    }
                }
        }
    }

    // Si todas las canciones ya existen localmente, llamar al callback con la lista de archivos
    if (archivosDescargados.size == generos.size) {
        callback(archivosDescargados, null)
    }
}*/