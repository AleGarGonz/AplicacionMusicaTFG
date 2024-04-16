package com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aplicacionmusicatfg.R

@Composable
fun MiFooter(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .padding(horizontal = 0.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Black.copy(alpha = 0.6f))
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterIconButton(drawableId = R.drawable.baseline_list_24, contentDescription = "Inicio")
            FooterIconButton(drawableId = R.drawable.baseline_search_24, contentDescription = "Buscar")
            FooterIconButton(drawableId = R.drawable.baseline_person_24, contentDescription = "Configuración")
        }
    }
}

@Composable
fun FooterIconButton(drawableId: Int, contentDescription: String) {
    IconButton(
        onClick = { /* Poner aqui lo que haga el boton */ },
        modifier = Modifier.height(40.dp),
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
    }
}