package com.example.aplicacionmusicatfg.view

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.getCancionStorage
import com.example.aplicacionmusicatfg.modelos.Cancion
import java.io.FileInputStream

//En esta pantalla de momento lo que hace es que reproduce todas las canciones
lateinit var mediaPlayer:MediaPlayer
@Composable
fun CancionScreen(navController: NavController,param1: String, param2: String, param3: String, param4: String, param5: String) {
    val cancion = Cancion()
    cancion.artista = param1
    cancion.audio = param2
    cancion.genero = param3
    cancion.imagen = param4
    cancion.titulo = param5
    println(cancion.artista)


    //No es un metodo para llamar
//Esto obtiene la cancion especifica para usarlo en la clase cancion
    getCancionStorage(cancion.audio) { archivo, excepcion ->
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
    }
    fun playClicked() {
        mediaPlayer.start()
    }
    PantallaCancion(
        onAnteriorClick ={},
        onStopClick={},
        onPlayClick={playClicked()},
        onSiguienteClick={}
    )

}




@Composable
fun PantallaCancion(
    onAnteriorClick: () -> Unit,
    onStopClick: () -> Unit,
    onPlayClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (!isPlaying) {
                    isPlaying = true
                }
                onAnteriorClick()
            }, modifier = Modifier.weight(1f)) {
                val anteriorIcon = painterResource(id = R.drawable.baseline_skip_previous_24)
                Icon(painter = anteriorIcon, contentDescription = "Anterior")
            }
            IconButton(onClick = {
                if (isPlaying) {
                    isPlaying = false
                    onPlayClick()
                }
                onStopClick()
            }, modifier = Modifier.weight(1f)) {
                val stopIcon = painterResource(id = R.drawable.baseline_stop_24)
                Icon(painter = stopIcon, contentDescription = "Stop")
            }
            IconButton(
                onClick = {
                    isPlaying = !isPlaying
                    onPlayClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                val playIcon = if (isPlaying) {
                    painterResource(id = R.drawable.baseline_pause_24)
                } else {
                    painterResource(id = R.drawable.baseline_play_arrow_24)
                }
                Icon(painter = playIcon, contentDescription = if (isPlaying) "Pause" else "Play")
            }
            IconButton(onClick = {
                if (!isPlaying) {
                    isPlaying = true
                }
                onSiguienteClick()
            }, modifier = Modifier.weight(1f)) {
                val siguienteIcon = painterResource(id = R.drawable.baseline_skip_next_24)
                Icon(painter = siguienteIcon, contentDescription = "Siguiente")
            }
        }
    }
}