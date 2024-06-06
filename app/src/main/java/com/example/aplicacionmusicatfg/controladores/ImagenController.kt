package com.example.aplicacionmusicatfg.controladores

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ImagenController: ViewModel() {
    val storageRef = FirebaseStorage.getInstance().reference
    fun getImagenStorage(context: Context, fileName: String, callback: (File?, Exception?) -> Unit) {
        // Crear un archivo local persistente en el directorio de almacenamiento interno de la aplicación
        val localFile = File(context.filesDir, "${fileName}")
        // Verificar si el archivo ya existe localmente
        if (localFile.exists()) {
            // Llamar al callback con el archivo local
            callback(localFile, null)
        } else {
            // Si el archivo no existe localmente, descargarlo de Firebase Storage
            val imgRef = storageRef.child("imagenes/${fileName}")
            imgRef.getFile(localFile)
                .addOnSuccessListener {
                    callback(localFile, null)
                }
                .addOnFailureListener { exception ->
                    Log.e("Descarga", "Fallida ${fileName}", exception)
                    callback(null, exception)
                }
        }
    }

    fun subirImagenStorage(file: File, onSuccess: () -> Unit, onCanceled: () -> Unit) {
        val imgRef = storageRef.child("imagenes/${file.name}")
        imgRef.putFile(Uri.fromFile(file))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnCanceledListener {
                onCanceled()
            }
            .addOnFailureListener { exception ->
            }
    }
}