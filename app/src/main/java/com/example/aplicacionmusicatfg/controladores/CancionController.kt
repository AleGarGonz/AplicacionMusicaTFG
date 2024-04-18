package com.example.aplicacionmusicatfg.controladores

import android.content.ContentValues
import android.util.Log
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File

///////////////////////////////////////////////////////////////////////////////////////////
//Realtime Database

//Devuelve la lista entera de canciones de realtime
fun getListaCancionesRealtime(callback: (List<Cancion>) -> Unit) {
    val database = Firebase.database
    val myRef = database.getReference("Canciones")

    // Read from the database
    myRef.addValueEventListener(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val canciones: MutableList<Cancion> = mutableListOf()

            for (cancionSnapshot in snapshot.children) {

                var artista = cancionSnapshot.child("Artista").getValue(String::class.java)
                val audio = cancionSnapshot.child("Audio").getValue(String::class.java)
                val genero = cancionSnapshot.child("Genero").getValue(String::class.java)
                val imagen = cancionSnapshot.child("Imagen").getValue(String::class.java)
                val titulo = cancionSnapshot.child("Titulo").getValue(String::class.java)

                if (artista != null && audio != null && genero != null && imagen != null && titulo != null) {

                    val cancion = Cancion()
                    cancion.artista = artista
                    cancion.audio = audio
                    cancion.genero = genero
                    cancion.imagen = imagen
                    cancion.titulo = titulo

                    canciones.add(cancion)
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
///////////////////////////////////////////////////////////////////////////////////////////
//Storage
//Descarga la cancion del Storage en formato MP3
fun getCancionStorage(fileName: String, callback: (File?, Exception?) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val audioRef = storageRef.child("audios/$fileName.mp3")

    // Crear un archivo local con el mismo nombre
    val localFile = File.createTempFile(fileName, ".mp3")

    audioRef.getFile(localFile)
        .addOnSuccessListener {
            // Descarga exitosa, llamar al callback con el archivo
            Log.e("Descarga", "Exitosa $fileName")
            callback(localFile, null)
        }
        .addOnFailureListener { exception ->
            // Manejar errores de descarga llamando al callback con la excepción
            Log.e("Descarga", "Fallida $fileName", exception)
            callback(null, exception)
        }
}
//Me devuelve las canciones que yo pase
fun getListaCancionStorage(canciones: List<Cancion>, callback: (List<File>?, Exception?) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference

    val archivosDescargados = mutableListOf<File>()
    val totalCanciones = canciones.size
    var cancionesDescargadas = 0

    canciones.forEach { cancion ->
        val audioRef = storageRef.child("audios/${cancion.audio}.mp3")

        // Crear un archivo local con el mismo nombre
        val localFile = File.createTempFile(cancion.audio, ".mp3")

        audioRef.getFile(localFile)
            .addOnSuccessListener {
                // Agregar el archivo descargado a la lista
                archivosDescargados.add(localFile)
                cancionesDescargadas++

                // Verificar si todas las canciones han sido descargadas
                if (cancionesDescargadas == totalCanciones) {
                    // Llamar al callback con la lista de archivos descargados
                    callback(archivosDescargados, null)
                }
            }
            .addOnFailureListener { exception ->
                // Manejar errores de descarga llamando al callback con la excepción
                Log.e("Descarga", "Fallida ${cancion.audio}", exception)

                // Verificar si todas las canciones han sido descargadas
                if (cancionesDescargadas == totalCanciones) {
                    // Llamar al callback con la lista de archivos descargados hasta ahora
                    callback(archivosDescargados, exception)
                }
            }
    }
}






//Me devuelve todas las canciones del storage en una lista formato MP3
fun getListaCancionesStorage(callback: (List<File>?, Exception?) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference.child("audios")

    storageRef.listAll()
        .addOnSuccessListener { listResult ->
            val files = mutableListOf<File>()
            val tasks = mutableListOf<Task<FileDownloadTask.TaskSnapshot>>()
            listResult.items.forEach { item ->
                val fileName = item.name
                val localFile = File.createTempFile(fileName, ".mp3")
                val task = item.getFile(localFile)
                    .addOnSuccessListener {
                        Log.e("Descarga", "Exitosa $fileName")
                        files.add(localFile)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Descarga", "Fallida $fileName", exception)
                    }
                tasks.add(task)
            }
            // Esperar a que todas las descargas se completen
            Tasks.whenAllComplete(tasks)
                .addOnSuccessListener {
                    callback(files, null)
                }
                .addOnFailureListener { exception ->
                    callback(null, exception)
                }
        }
        .addOnFailureListener { exception ->
            Log.e("Descarga", "Error al obtener la lista de archivos", exception)
            callback(null, exception)
        }
}

///////////////////////////////////////////////////////////////////////////////////////////
//Buscar
fun buscarCancionesPorTitulo(titulo: String, callback: (List<Cancion>) -> Unit) {
    val database = Firebase.database
    val myRef = database.getReference("Canciones")
    val query = myRef.orderByChild("Titulo").startAt(titulo).endAt(titulo + "\uf8ff")
    query.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val canciones: MutableList<Cancion> = mutableListOf()
            for (cancionSnapshot in snapshot.children) {
                val artista = cancionSnapshot.child("Artista").getValue(String::class.java)
                val audio = cancionSnapshot.child("Audio").getValue(String::class.java)
                val genero = cancionSnapshot.child("Genero").getValue(String::class.java)
                val imagen = cancionSnapshot.child("Imagen").getValue(String::class.java)
                val titulo = cancionSnapshot.child("Titulo").getValue(String::class.java)
                if (artista != null && audio != null && genero != null && imagen != null && titulo != null) {
                    val cancion = Cancion()
                    cancion.artista = artista
                    cancion.audio = audio
                    cancion.genero = genero
                    cancion.imagen = imagen
                    cancion.titulo = titulo
                    canciones.add(cancion)
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
    val database = Firebase.database
    val myRef = database.getReference("Canciones")
    val query = myRef.orderByChild("Artista").startAt(artista).endAt(artista + "\uf8ff")
    query.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val canciones: MutableList<Cancion> = mutableListOf()
            for (cancionSnapshot in snapshot.children) {
                val artista = cancionSnapshot.child("Artista").getValue(String::class.java)
                val audio = cancionSnapshot.child("Audio").getValue(String::class.java)
                val genero = cancionSnapshot.child("Genero").getValue(String::class.java)
                val imagen = cancionSnapshot.child("Imagen").getValue(String::class.java)
                val titulo = cancionSnapshot.child("Titulo").getValue(String::class.java)

                if (artista != null && audio != null && genero != null && imagen != null && titulo != null) {
                    val cancion = Cancion()
                    cancion.artista = artista
                    cancion.audio = audio
                    cancion.genero = genero
                    cancion.imagen = imagen
                    cancion.titulo = titulo
                    canciones.add(cancion)
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



///////////////////////////////////////////////////////////////////////////////////////////

//No es un metodo para llamar
//Esto obtiene la cancion especifica para usarlo en la clase cancion
/*getCancionStorage("Ave Maria (2007 Version) - David Bisbal") { archivo, excepcion ->
    if (excepcion != null) {
        Log.e("Error", "Error al obtener la canción", excepcion)
    } else {
        if (archivo != null) {
            // Inicializar MediaPlayer si aún no se ha inicializado
            if (!::mediaPlayer.isInitialized) {
                mediaPlayer = MediaPlayer()
            } else {
                // Detener y resetear MediaPlayer si ya está reproduciendo una canción
                mediaPlayer.stop()
                mediaPlayer.reset()
            }
            try {
                // Configurar MediaPlayer con el archivo de la canción descargada
                val fis = FileInputStream(archivo)
                val fd = fis.fd
                mediaPlayer.setDataSource(fd)
                fis.close()

                // Preparar y reproducir la canción
                mediaPlayer.prepare()
            } catch (e: Exception) {
                Log.e("Error", "Error al reproducir la canción", e)
            }
        } else {
            // Manejar el caso en el que no se obtuvo ningún archivo
            Log.e("Error", "No se obtuvo ningún archivo")
        }
    }
}*/