package com.example.aplicacionmusicatfg.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.MusicPlayerController
import com.example.aplicacionmusicatfg.controladores.getCancionStorage
import com.example.aplicacionmusicatfg.controladores.getImagenStorage
import com.example.aplicacionmusicatfg.modelos.Cancion
import kotlinx.coroutines.delay
import java.io.File


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
    var ImagenFile: File? = null;
    var pantalllaVisible by rememberSaveable { mutableStateOf(false) }

    //Usar un objeto Cancion Controlador e Imagen Controllador

    getCancionStorage(LocalContext.current,cancion.audio) { archivo, excepcion ->
        if (excepcion != null) {
            println("Error al descargar archivos: ${excepcion.message}")
        } else {
            CancionFile = archivo
        }
    }
    getImagenStorage(LocalContext.current,cancion.imagen) { archivo, excepcion ->
        if (excepcion != null) {
            println("Error al descargar archivos: ${excepcion.message}")
        } else {
            ImagenFile = archivo
        }
    }
    if(!pantalllaVisible){
        LaunchedEffect(validarImagen(ImagenFile) && validarCancion(CancionFile)){
            pantalllaVisible = true
        }
    }
    if(pantalllaVisible) {
        musicPlayerController.setFile(CancionFile)
        PantallaCancion2(
            onStopClick = { musicPlayerController.stopAndReset() },
            onPlayClick = { musicPlayerController.playOrPauseOneFile() },
            musicPlayerController = musicPlayerController,
            cancion = cancion,
            imagen = ImagenFile
        )
        BackHandler(onBack = {//Detecta cuando vas para atras lo podria añadir en las pantallas principales
            navController.popBackStack()
            musicPlayerController.release()
        })
    }else{
        CircularProgressIndicator(
            modifier = Modifier
                .size(90.dp)
                .padding(60.dp),
            color = Color.Blue,
            strokeWidth = 12.dp,
        )
    }
}




@Composable
fun PantallaCancion2(
    onStopClick: () -> Unit,
    onPlayClick: () -> Unit,
    cancion: Cancion,
    imagen:File?,
    musicPlayerController: MusicPlayerController
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var progress by rememberSaveable { mutableStateOf(0f) }
    var seekPosition by rememberSaveable { mutableStateOf(0) }
    var seekPositionUpdated by rememberSaveable { mutableStateOf(false)}

    LaunchedEffect(progress) {
        if (seekPositionUpdated) {
            delay(300)
            musicPlayerController.seekTo(seekPosition)
            seekPositionUpdated = false
            musicPlayerController.playOrPauseOneFile()
        }
    }
    LaunchedEffect(isPlaying && progress == progress) {
        while (isPlaying) {
            delay(100)
            val currentPosition = musicPlayerController.getCurrentPosition()
            val duration = musicPlayerController.getDuration()
            if (currentPosition > 0) {
                val newProgress = currentPosition.toFloat() / duration.toFloat()
                progress = newProgress.coerceIn(0f, 1f)
            }
            if(progress > 0.990){
                onStopClick()
                isPlaying = false;
                break;
            }
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
                text = cancion.titulo,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = cancion.artista,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            if(validarImagen(imagen)){
                // Lee el archivo en un Bitmap
                val bitmap: Bitmap = BitmapFactory.decodeFile(imagen?.absolutePath)

                // Convierte el Bitmap en un ImageBitmap
                val imageBitmap = bitmap.asImageBitmap()
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Descripción de la imagen",
                    modifier = Modifier
                        .height(240.dp)
                        .width(240.dp)
                )
            }else{
                Image(//Siempre de primeras muestra la imagen esta por defecto no se si es que no hace bien la descarga o que, luego los demas inicios sale la imagen cargada
                    painter = painterResource(id = R.drawable.pordefecto),
                    contentDescription = "Descripción de la imagen",
                    modifier = Modifier
                        .height(240.dp)
                        .width(240.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Slider(
                value = progress,
                onValueChange = { newValue ->
                    seekPosition = (musicPlayerController.getDuration() * newValue).toInt()
                    seekPositionUpdated = true
                    progress = newValue
                    musicPlayerController.stopAndReset()
                    if(!isPlaying){
                        isPlaying=true
                    }
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

fun validarImagen(ImagenFile: File?): Boolean {
    return ImagenFile != null &&
            ImagenFile.isFile &&
            ImagenFile.exists() &&
            ImagenFile.length() > 0L
}

fun validarCancion(AudioFile: File?): Boolean {
    return AudioFile != null &&
            AudioFile.isFile &&
            AudioFile.exists() &&
            AudioFile.length() > 0L
}