package com.example.aplicacionmusicatfg.view

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorArtista
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorTitulo
import com.example.aplicacionmusicatfg.controladores.getListaCancionStorage
import com.example.aplicacionmusicatfg.modelos.Cancion
import java.io.File

@Composable
fun BusquedaScreen() {
    var searchText by remember { mutableStateOf("") }
    var canciones by remember { mutableStateOf(emptyList<Cancion>()) }
    var listaDeArchivos: List<File> by remember { mutableStateOf(emptyList()) }
    val cancionActualIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(searchText) {
        // Realizar búsqueda por título
        buscarCancionesPorTitulo(searchText) { cancionesPorTitulo ->
            // Realizar búsqueda por artista
            buscarCancionesPorArtista(searchText) { cancionesPorArtista ->
                // Combinar ambas listas y eliminar duplicados
                val cancionesCombinadas = (cancionesPorTitulo + cancionesPorArtista).distinctBy { it.titulo }
                canciones = cancionesCombinadas
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar canciones por título o artista") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* No es necesario hacer nada aquí */ }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Column(modifier = Modifier.fillMaxSize()) {
            if (canciones.isNotEmpty()) {
                getListaCancionStorage(canciones = canciones) { archivosDescargados, exception ->
                    if (exception != null) {
                        // Manejar la excepción si ocurrió algún error durante la descarga
                        println("Error al descargar archivos: ${exception.message}")
                    } else {
                        // Se descargaron los archivos exitosamente
                        archivosDescargados?.let { archivos ->
                            // Asignar la lista de archivos descargados a la lista global
                            listaDeArchivos = archivos
                        }
                    }
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(canciones) { cancion ->
                        CancionItem(cancion = cancion,listaDeArchivos,cancionActualIndex)
                    }
                }
            } else {
                Text(text = "No se encontraron canciones", fontSize = 16.sp, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancionItem(cancion: Cancion,listCanciones:List<File>?,cancionActualIndex:Int) {
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { isSheetOpen = true }
    ) {
        Text(text = "Título: ${cancion.titulo}", fontSize = 18.sp)
        Text(text = "Artista: ${cancion.artista}", fontSize = 16.sp)
        if(isSheetOpen){
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                    mediaPlayer?.pause()
                    mediaPlayer?.seekTo(0)

                }) {

                fun playClicked() {
                    if (listCanciones != null) {
                        println(listCanciones[0].absoluteFile)//Sunflower (Spider-Man_ Into the Spider-Verse) - Post Malone & Swae Lee7928679051577060452.mp3 el numero va variando
                        val archivoActual = listCanciones[cancionActualIndex]//Tengo que conseguir la posicion de alguna manera
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
                fun stopClicked() {
                    mediaPlayer?.pause()
                    mediaPlayer?.seekTo(0)
                }
                PantallaCancion(
                    onAnteriorClick ={},
                    onStopClick={stopClicked()},
                    onPlayClick={playClicked()},
                    onSiguienteClick={}
                )
            }
        }
    }
}

