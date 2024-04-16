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
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.getListaCancionesStorage
import java.io.File

//En esta pantalla de momento lo que hace es que reproduce todas las canciones

@Composable
fun CancionScreen() {
    var listaDeArchivos: List<File>? by remember { mutableStateOf(null) }
    var cancionActualIndex by remember { mutableStateOf(0) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    // Obtener la lista de canciones
    if (listaDeArchivos == null) {
        getListaCancionesStorage { archivos, excepcion ->
            if (excepcion != null) {
                // Manejar el error
                Log.e("Error", "Error al obtener la lista de canciones", excepcion)
            } else {
                listaDeArchivos = archivos
            }
        }
    }
    // Función para reproducir la canción actual
    fun playClicked() {
        if (listaDeArchivos != null) {
            val archivoActual = listaDeArchivos!![cancionActualIndex]
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setDataSource(archivoActual.path)
                mediaPlayer!!.setOnPreparedListener { mp ->
                    mp.start()
                }
                mediaPlayer!!.prepare()
            } else {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.pause()
                } else {
                    mediaPlayer!!.start()
                }
            }
        }
    }

    // Función para detener la reproducción de la canción
    fun stopClicked() {
        mediaPlayer?.seekTo(0)
        mediaPlayer?.pause()
    }

    // Función para avanzar a la siguiente canción
    fun siguienteCancion() {
        if (listaDeArchivos != null) {
            cancionActualIndex = (cancionActualIndex + 1) % listaDeArchivos!!.size
            mediaPlayer?.stop()
            mediaPlayer = null
            playClicked()
        }
    }

    // Función para avanzar a la siguiente canción
    fun anteriorCancion() {
        if (listaDeArchivos != null) {
            // Calcular el índice de la canción anterior
            var nuevoIndice = cancionActualIndex - 1
            if (nuevoIndice < 0) {
                nuevoIndice = listaDeArchivos!!.size - 1
            }
            cancionActualIndex = nuevoIndice

            // Detener la reproducción de la canción actual
            mediaPlayer?.stop()
            mediaPlayer = null

            // Reproducir la nueva canción
            playClicked()
        }
    }

    PantallaCancion(
        onAnteriorClick ={anteriorCancion()},
        onStopClick={stopClicked()},
        onPlayClick={playClicked()},
        onSiguienteClick={siguienteCancion()}
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