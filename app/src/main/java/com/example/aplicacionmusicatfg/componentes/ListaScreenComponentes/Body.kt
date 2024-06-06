package com.example.aplicacionmusicatfg.componentes.ListaScreenComponentes

import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.CancionItem
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.validarAudioLista
import com.example.aplicacionmusicatfg.componentes.ListasDeReproScreenComponentes.listasreprocontroller
import com.example.aplicacionmusicatfg.controladores.CancionController
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.MusicPlayerController
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.example.aplicacionmusicatfg.modelos.ListaReproduccion
import com.example.aplicacionmusicatfg.navigation.Rutas
import java.io.File

private val musicPlayerController = MusicPlayerController()
private val cancionController = CancionController()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Body(navController: NavController, loginController: LoginController, ListaID: String) {
    val uid = loginController.getCurrentUser()?.uid.toString()
    var listaRepro by remember { mutableStateOf(ListaReproduccion()) }
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
        cancionController.buscarCancionesPorTitulo(searchText) { cancionesPorTitulo ->
            cancionController.buscarCancionesPorArtista(searchText) { cancionesPorArtista ->
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
            Spacer(modifier = Modifier.height(12.dp))
            var esMia by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                listasreprocontroller.comprobarExistenciaListaReproduccion(uid, ListaID) { existe ->
                    esMia = existe
                }
            }
            if (esMia) {
                BotonAnadirCanciones {
                    navController.navigate(
                        route = Rutas.AnadirCanciones.createRoute(
                            ListaID
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                color = Color.White,
                thickness = 2.dp,
                modifier = Modifier.width(360.dp).padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (canciones.isNotEmpty()) {
                cancionController.getListaSinConexionCancionStorage(LocalContext.current,canciones = canciones) { archivosDescargados, exception ->
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

@Composable
private fun BotonAnadirCanciones(onClick: () -> Unit) {
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
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
    }
}