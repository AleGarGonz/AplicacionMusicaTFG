package com.example.aplicacionmusicatfg.componentes.AnadirCancionesComponentes

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.Reproductor
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.obtenerPosicionArchivo
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.validarAudioLista
import com.example.aplicacionmusicatfg.componentes.ListasDeReproComponentes.listasreprocontroller
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.MusicPlayerController
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorArtista
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorTitulo
import com.example.aplicacionmusicatfg.controladores.getListaSinConexionCancionStorage
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.example.aplicacionmusicatfg.modelos.ListaReproduccion
import java.io.File

private val musicPlayerController = MusicPlayerController()
var listaRepro = ListaReproduccion()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Body(navController: NavController, loginController: LoginController, ListaID: String) {
    val uid = loginController.getCurrentUser()?.uid.toString()
    var searchText by remember { mutableStateOf("") }
    var canciones by remember { mutableStateOf(emptyList<Cancion>()) }
    var listaDeArchivos: List<File> by remember { mutableStateOf(emptyList()) }
    var lazyColumnVisible by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        listasreprocontroller.descargarListaReproduccion(ListaID) { lista ->
            listaRepro= lista!!
        }
    }
    LaunchedEffect(searchText) {
        buscarCancionesPorTitulo(searchText) { cancionesPorTitulo ->
            buscarCancionesPorArtista(searchText) { cancionesPorArtista ->
                val cancionesCombinadas = (cancionesPorTitulo + cancionesPorArtista).distinctBy { it.titulo }

                val cancionesFiltradas = cancionesCombinadas.filter { cancion ->
                    cancion.titulo !in listaRepro.Canciones
                }
                canciones = cancionesFiltradas
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(140.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Cyan.copy(alpha = 0.9f), Color.Black),
                )
            )
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = listaRepro.Listanombre,
            modifier = Modifier
                .padding(start = 6.dp)
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp),
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color =Color.Black
            )
        )
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar canciones por título o artista") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {}),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 5.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                containerColor = Color(0xFFFAFAFA),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                color = Color.White,
                thickness = 2.dp,
                modifier = Modifier.width(360.dp).padding(start = 12.dp)
            )
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
                            AñadirCancionItem(cancion = cancion,listaDeArchivos,musicPlayerController,uid)
                        }
                    }
                }
            } else {
                Text(text = "No se encontraron canciones", fontSize = 16.sp, modifier = Modifier.padding(16.dp),color =Color.Black)
            }
        }
    }
    BackHandler(onBack = {
        listaRepro.Canciones = emptyList()
        navController.popBackStack()
    })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AñadirCancionItem(cancion: Cancion, listCanciones:List<File>, musicPlayerController: MusicPlayerController,uid:String) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var context = LocalContext.current
    val onAnteriorClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playPrevious() }}
    val onStopClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.stopAndReset() }}
    val onPlayClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playOrPause()} }
    val onSiguienteClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playNext()} }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start=19.dp).padding(end = 26.dp).padding(vertical = 4.dp)
            .clickable {
                isSheetOpen = true
                musicPlayerController.setListAndIndex(
                    listCanciones,
                    obtenerPosicionArchivo(listCanciones, cancion.audio)
                )
            },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Cyan)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.background(Color.White)) {
            var isAdded by remember { mutableStateOf(false) }
            Column(
                Modifier
                    .padding(start = 8.dp).padding(top = 3.dp)
                    .height(60.dp)
                    .width(300.dp)
            ) {
                Text(
                    text = "${cancion.titulo}",
                    fontSize = 21.sp,
                    color = Color.Black
                )
                Text(
                    text = "${cancion.artista}",
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }
            IconButton(
                onClick = {
                    listaRepro.Canciones += cancion.titulo
                    listasreprocontroller.actualizarListaReproduccion(listaRepro,uid, listaRepro.id)
                    Toast.makeText(context, "Canción Añadida", Toast.LENGTH_SHORT).show()
                    isAdded = true
                },
                modifier = Modifier.weight(1f),
                enabled = !isAdded
            ) {
                val stopIcon = painterResource(id = R.drawable.baseline_add_24)
                Icon(
                    painter = stopIcon,
                    contentDescription = "Añadir",
                    tint = if (!isAdded) Color.Black else Color.LightGray
                )
            }

        }
        if(isSheetOpen){
            ModalBottomSheet(
                containerColor= Color.Cyan,
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

