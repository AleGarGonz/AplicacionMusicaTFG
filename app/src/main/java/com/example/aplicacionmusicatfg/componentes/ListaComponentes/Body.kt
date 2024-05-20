package com.example.aplicacionmusicatfg.componentes.ListaComponentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.componentes.ListasDeReproComponentes.listasreprocontroller
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.MusicPlayerController
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorArtista
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorTitulo
import com.example.aplicacionmusicatfg.controladores.getListaSinConexionCancionStorage
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.example.aplicacionmusicatfg.modelos.ListaReproduccion
import com.example.aplicacionmusicatfg.navigation.Rutas
import java.io.File

private val musicPlayerController = MusicPlayerController()
@Composable
fun Body(navController: NavController, loginController: LoginController, ListaID: String) {
    val uid = loginController.getCurrentUser()?.uid.toString()
    var listaRepro by remember { mutableStateOf(ListaReproduccion()) }

    var searchText by remember { mutableStateOf("") }
    var canciones by remember { mutableStateOf(emptyList<Cancion>()) }
    var listaDeArchivos: List<File> by remember { mutableStateOf(emptyList()) }
    var lazyColumnVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        listasreprocontroller.descargarListaReproduccion(uid,ListaID) { lista ->
            listaRepro= lista!!
        }
    }
    LaunchedEffect(searchText) {
        buscarCancionesPorTitulo(searchText) { cancionesPorTitulo ->
            buscarCancionesPorArtista(searchText) { cancionesPorArtista ->
                val cancionesCombinadas = (cancionesPorTitulo + cancionesPorArtista).distinctBy { it.titulo }

                val cancionesFiltradas = cancionesCombinadas.filter { cancion ->
                    listaRepro.Canciones.any { nombreCancion ->
                        cancion.titulo == nombreCancion
                    }
                }
                canciones = cancionesFiltradas
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = listaRepro.Listanombre,
            modifier = Modifier
                .padding(start = 6.dp)
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )
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
            BotonAñadirCanciones(){
                navController.navigate(
                    route = Rutas.AnadirCanciones.createRoute(
                        ListaID
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(8.dp))
            if (canciones.isNotEmpty()) {
                getListaSinConexionCancionStorage(LocalContext.current,canciones = canciones) { archivosDescargados, exception ->
                    if (exception != null) {
                        println("Error al descargar archivos: ${exception.message}")
                    } else {
                        archivosDescargados?.let { archivos ->
                            listaDeArchivos = archivos
                        }
                    }
                }
                if(!lazyColumnVisible){
                    LaunchedEffect(validarAudioLista(listaDeArchivos)){
                        lazyColumnVisible = true
                    }
                }
                if(lazyColumnVisible){
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(canciones) { cancion ->
                            CancionItem(cancion = cancion,listaDeArchivos,musicPlayerController,navController)
                        }
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
private fun CancionItem(cancion: Cancion, listCanciones:List<File>, musicPlayerController: MusicPlayerController, navController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val onAnteriorClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playPrevious() }}
    val onStopClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.stopAndReset() }}
    val onPlayClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playOrPause()} }
    val onSiguienteClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playNext()} }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 23.dp)
            .padding(end = 23.dp)
            .padding(vertical = 6.dp)
            .clickable {
                isSheetOpen = true
                musicPlayerController.setListAndIndex(
                    listCanciones,
                    obtenerPosicionArchivo(listCanciones, cancion.audio)
                )
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier
                    .padding(start = 8.dp)
                    .width(300.dp)
            ) {
                Text(text = "${cancion.titulo}", fontSize = 21.sp)
                Text(text = "${cancion.artista}", fontSize = 17.sp)
            }
            IconButton(
                onClick = {
                    navController.navigate(
                        route = Rutas.CancionScreen.createRoute(
                            cancion.artista,
                            cancion.audio,
                            cancion.genero,
                            cancion.imagen,
                            cancion.titulo
                        )
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                val stopIcon = painterResource(id = R.drawable.baseline_open_in_new_24)
                Icon(painter = stopIcon, contentDescription = "MasInfo")
            }

        }
        if(isSheetOpen){
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                    musicPlayerController.release()

                }) {
                if(listCanciones.size >=1){
                    Reproductor(
                        onAnteriorClick = onAnteriorClick,
                        onStopClick = onStopClick,
                        onPlayClick = onPlayClick,
                        onSiguienteClick = onSiguienteClick
                    )
                }else{
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "No se puede reproducir la canción en estos momentos",
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                }
            }
        }
    }
}
//Metodo para obtener la posicion del mp3 en la lista
private fun obtenerPosicionArchivo(files: List<File>, nombreArchivo: String): Int {
    for ((index, file) in files.withIndex()) {
        if (file.absoluteFile.name.contains(nombreArchivo)) {
            return index
        }
    }
    return -1
}
@Composable
private fun Reproductor(
    onAnteriorClick: () -> Unit,
    onStopClick: () -> Unit,
    onPlayClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }

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

private fun validarAudioLista(listaArchivos: List<File?>): Boolean {
    return listaArchivos.all {
        it != null &&
                it.isFile &&
                it.exists() &&
                it.length() > 0L
    }
}

@Composable
private fun BotonAñadirCanciones(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Cyan),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(start = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Añadir Canciones",
                style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                contentDescription = "Añadir",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}