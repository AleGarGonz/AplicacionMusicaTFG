package com.example.aplicacionmusicatfg.componentes.CancionScreenComponentes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.CancionController
import com.example.aplicacionmusicatfg.controladores.ImagenController
import com.example.aplicacionmusicatfg.controladores.MusicPlayerController
import com.example.aplicacionmusicatfg.modelos.Cancion
import kotlinx.coroutines.delay
import java.io.File

private val musicPlayerController = MusicPlayerController()
private val cancionController = CancionController()
@Composable
fun Body(navController: NavController,cancion: Cancion) {
    var CancionFile: File? = null;
    var ImagenFile: File? = null;
    var pantalllaVisible by rememberSaveable { mutableStateOf(false) }
    val imagenController = ImagenController()

    cancionController.getCancionStorage(LocalContext.current,cancion.audio) { archivo, excepcion ->
        if (excepcion != null) {
            println("Error al descargar archivos: ${excepcion.message}")
        } else {
            CancionFile = archivo
        }
    }
    imagenController.getImagenStorage(LocalContext.current,cancion.imagen) { archivo, excepcion ->
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
        Contenido(
            onStopClick = { musicPlayerController.stopAndReset() },
            onPlayClick = { musicPlayerController.playOrPauseOneFile() },
            musicPlayerController = musicPlayerController,
            cancion = cancion,
            imagen = ImagenFile
        )
    }else{
        //En caso de que llegue aqui y no se haya descargado bien la información, mostrara este CircularProgressIndicator
        CircularProgressIndicator(
            modifier = Modifier
                .size(90.dp)
                .padding(60.dp),
            color = Color.Blue,
            strokeWidth = 12.dp,
        )
    }
    //Si la canción se esta reproduciendo y el usuario decide retroceder a la pantalla anterior, esto eliminara
    //el recurso de la cancion de ese momento del musicPlayerController para evitar que se solapen varios audios
    // y algunos problemas varios
    BackHandler(onBack = {
        navController.popBackStack()
        musicPlayerController.release()
    })
}




@Composable
private fun Contenido(
    onStopClick: () -> Unit,
    onPlayClick: () -> Unit,
    cancion: Cancion,
    imagen: File?,
    musicPlayerController: MusicPlayerController
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var progress by rememberSaveable { mutableStateOf(0f) }
    var seekPosition by rememberSaveable { mutableStateOf(0) }
    var seekPositionUpdated by rememberSaveable { mutableStateOf(false) }

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
            .fillMaxSize()
            .height(140.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Cyan.copy(alpha = 0.9f), Color.Black)
                )
            )
    )
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
                fontSize = 35.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = cancion.artista,
                fontSize = 19.sp,
                color = Color.Black
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
                Image(
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
                    Icon(painter = stopIcon, contentDescription = "Stop", tint = Color.White)
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
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

private fun validarImagen(ImagenFile: File?): Boolean {
    return ImagenFile != null &&
            ImagenFile.isFile &&
            ImagenFile.exists() &&
            ImagenFile.length() > 0L
}

private fun validarCancion(AudioFile: File?): Boolean {
    return AudioFile != null &&
            AudioFile.isFile &&
            AudioFile.exists() &&
            AudioFile.length() > 0L
}