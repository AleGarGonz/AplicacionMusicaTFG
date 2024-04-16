package com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmusicatfg.R

@Composable
fun MiHeader(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(5.dp).padding(start = 6.dp),
            text = "Descubre",
            style = TextStyle(
                fontSize = 28.sp, // Tamaño de fuente más grande
                fontWeight = FontWeight.Bold, // Tipo de letra en negrita
                fontFamily = FontFamily.SansSerif // Opcional: elige una fuente diferente si lo deseas
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        HeaderIconButton(drawableId = R.drawable.baseline_settings_24, contentDescription = "Configuracion")
    }
}

@Composable
fun HeaderIconButton(drawableId: Int, contentDescription: String) {
    IconButton(
        onClick = { /* Acción al hacer clic en el botón */ },
        modifier = Modifier.padding(top=5.dp),
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription,
            modifier = Modifier.size(30.dp),
            tint = Color.Black
        )
    }
}