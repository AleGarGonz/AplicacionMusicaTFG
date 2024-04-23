package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.getCancionStorage
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.example.aplicacionmusicatfg.modelos.MusicPlayerController
import kotlinx.coroutines.delay
import java.io.File


//Reestructurar Busqueda
//Y terminar esta

private val musicPlayerController = MusicPlayerController()
@Composable
fun CancionScreen(navController: NavController,param1: String, param2: String, param3: String, param4: String, param5: String) {
    val cancion = Cancion()
    cancion.artista = param1
    cancion.audio = param2
    cancion.genero = param3
    cancion.imagen = param4
    cancion.titulo = param5

    var CancionFile: File? = null;


    //No es un metodo para llamar
    //Esto obtiene la cancion especifica para usarlo en la clase cancion
    getCancionStorage(LocalContext.current,cancion.audio) { archivo, excepcion ->
        if (excepcion != null) {
            println("Error al descargar archivos: ${excepcion.message}")
        } else {
            CancionFile = archivo
        }
    }
    musicPlayerController.setFile(CancionFile)
    PantallaCancion2(

        onStopClick={ musicPlayerController.stopAndReset()},
        onPlayClick={ musicPlayerController.playOrPauseOneFile()},
        musicPlayerController = musicPlayerController
    )

}




@Composable
fun PantallaCancion2(
    onStopClick: () -> Unit,
    onPlayClick: () -> Unit,
    musicPlayerController: MusicPlayerController // Se pasa el controlador como argumento
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }

    var seekPosition by remember { mutableStateOf(0) }
    var seekPositionUpdated by remember { mutableStateOf(false) }

    LaunchedEffect(progress) {
        if (seekPositionUpdated) {
            delay(500) // Espera un segundo antes de realizar el cambio de seek
            musicPlayerController.seekTo(seekPosition)
            seekPositionUpdated = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Título de la canción",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Slider(
                value = progress,
                onValueChange = { newValue ->
                    seekPosition = (musicPlayerController.getDuration() * newValue).toInt()
                    seekPositionUpdated = true
                    progress = newValue
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    if (isPlaying) {
                        isPlaying = false
                        onPlayClick()
                    }
                    onStopClick()
                    progress = 0f
                }) {
                    val stopIcon = painterResource(id = R.drawable.baseline_stop_24)
                    Icon(painter = stopIcon, contentDescription = "Stop")
                }
                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        onPlayClick()
                    }
                ) {
                    val playIcon = if (isPlaying) {
                        painterResource(id = R.drawable.baseline_pause_24)
                    } else {
                        painterResource(id = R.drawable.baseline_play_arrow_24)
                    }
                    Icon(
                        painter = playIcon,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
            }
        }
    }
}