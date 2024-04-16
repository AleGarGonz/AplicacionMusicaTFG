package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes.MiBody
import com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes.MiFooter
import com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes.MiHeader


//Pantalla con las categorias y el buscador
@Composable
fun GenerosScreen() {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        MiBody(modifier = Modifier.align(Alignment.Center))
        MiHeader(Modifier.align(Alignment.TopEnd))
        MiFooter(Modifier.align(Alignment.BottomCenter))
    }
}