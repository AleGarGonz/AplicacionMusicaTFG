package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.CancionItem
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.validarAudioLista
import com.example.aplicacionmusicatfg.controladores.MusicPlayerController
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorArtista
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorTitulo
import com.example.aplicacionmusicatfg.controladores.getListaSinConexionCancionStorage
import com.example.aplicacionmusicatfg.modelos.Cancion
import java.io.File

private val musicPlayerController = MusicPlayerController()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaScreen(navController: NavController,genero: String?) {
    var searchText by remember { mutableStateOf("") }
    var canciones by remember { mutableStateOf(emptyList<Cancion>()) }
    var listaDeArchivos: List<File> by remember { mutableStateOf(emptyList()) }
    var lazyColumnVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(searchText) {
        if(genero!!.contains("Def")){
            // Realizar búsqueda por título
            buscarCancionesPorTitulo(searchText) { cancionesPorTitulo ->
                // Realizar búsqueda por artista
                buscarCancionesPorArtista(searchText) { cancionesPorArtista ->
                    // Combinar ambas listas y eliminar duplicados
                    val cancionesCombinadas = (cancionesPorTitulo + cancionesPorArtista).distinctBy { it.titulo }
                    canciones = cancionesCombinadas
                }
            }
        }else{
            buscarCancionesPorTitulo(searchText) { cancionesPorTitulo ->
                buscarCancionesPorArtista(searchText) { cancionesPorArtista ->
                    val cancionesCombinadas = (cancionesPorTitulo + cancionesPorArtista).distinctBy { it.titulo }

                    val cancionesFiltradas = cancionesCombinadas.filter { cancion ->
                        cancion.genero == genero // Filtrar por género
                    }

                    canciones = cancionesFiltradas
                }
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
        if(!genero!!.contains("Def")){
            Text(
                text = genero,
                modifier = Modifier
                    .padding(start= 6.dp)
                    .padding(top = 12.dp)
                    .padding(horizontal = 12.dp),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color =Color.Black
                )
            )
        }else{
            Text(
                text = "Canciones",
                modifier = Modifier
                    .padding(start= 6.dp)
                    .padding(top = 8.dp)
                    .padding(horizontal = 12.dp),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color =Color.Black
                )
            )
        }
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar canciones por título o artista") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {}),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp).padding(top = 5.dp),
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
        Text(
            text = "¿Qué quieres escuchar hoy?",
            modifier = Modifier
                .padding(start= 6.dp)
                .padding(top = 12.dp)
                .padding(bottom = 4.dp)
                .padding(horizontal = 12.dp),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color =Color.Black
            )
        )
        Column(modifier = Modifier.fillMaxSize()) {
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
                Text(text = "No se encontraron canciones", fontSize = 16.sp, modifier = Modifier.padding(16.dp),color = Color.Black)
            }
        }
    }
}