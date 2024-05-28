package com.example.aplicacionmusicatfg.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.CancionScreenComponentes.Body
import com.example.aplicacionmusicatfg.modelos.Cancion

@Composable
fun CancionScreen(navController: NavController,param1: String, param2: String, param3: String, param4: String, param5: String) {
    val cancion = Cancion()
    cancion.artista = param1
    cancion.audio = param2
    cancion.genero = param3
    cancion.imagen = param4
    cancion.titulo = param5
    Body(navController,cancion)
}