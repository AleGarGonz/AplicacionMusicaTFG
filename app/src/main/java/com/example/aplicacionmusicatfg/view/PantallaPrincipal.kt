package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes.MiBody
import com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes.MiFooter
import com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes.MiHeader


//Pantalla con las categorias y el buscador
@Composable
fun GenerosScreen(navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        MiBody(modifier = Modifier.align(Alignment.Center),navController)
        MiHeader(Modifier.align(Alignment.TopEnd))
        MiFooter(Modifier.align(Alignment.BottomCenter))
    }
}