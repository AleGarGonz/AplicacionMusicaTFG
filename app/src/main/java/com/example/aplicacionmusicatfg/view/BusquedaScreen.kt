package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorArtista
import com.example.aplicacionmusicatfg.controladores.buscarCancionesPorTitulo
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.example.aplicacionmusicatfg.navigation.Rutas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaScreen(navController:NavController) {
    var searchText by remember { mutableStateOf("") }
    var canciones by remember { mutableStateOf(emptyList<Cancion>()) }

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
        // Barra de búsqueda
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Buscar canciones por título o artista") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* No es necesario hacer nada aquí */ }),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )

        // Lista de canciones
        Column(modifier = Modifier.fillMaxSize()) {
            if (canciones.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(canciones) { cancion ->
                        // Hacer cada elemento de la lista clicleable sin acción definida
                        CancionItem(cancion = cancion,navController)
                    }
                }
            } else {
                Text(text = "No se encontraron canciones", fontSize = 16.sp, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun CancionItem(cancion: Cancion,navController:NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable {navController.navigate(route = Rutas.CancionScreen.createRoute(
                cancion.artista,cancion.audio,cancion.genero,cancion.imagen,cancion.titulo))}
    ) {
        Text(text = "Título: ${cancion.titulo}", fontSize = 18.sp)
        Text(text = "Artista: ${cancion.artista}", fontSize = 16.sp)
        // Agregar más detalles de la canción si es necesario
    }
}

