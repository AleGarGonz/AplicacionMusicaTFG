package com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aplicacionmusicatfg.R
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiBody(modifier: Modifier) {
    val searchText = remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Cyan.copy(alpha=0.9f), Color.Black),
                )
            )
    ) {
        OutlinedTextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 70.dp)
                .background(Color.White),
            label = { Text("Canciones") },
            leadingIcon = {
                val icono = painterResource(id = R.drawable.baseline_search_24)
                Icon(painter = icono, contentDescription = "Buscar")
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color.Black,
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Text(
            text = "Explorar géneros",
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 12.dp),
            style = TextStyle(
                fontSize = 20.sp, // Tamaño de fuente más grande
                fontWeight = FontWeight.Bold, // Tipo de letra en negrita
                fontFamily = FontFamily.SansSerif // Opcional: elige una fuente diferente si lo deseas
            )
        )

        GenreList(modifier = Modifier.fillMaxWidth())
    }
}


@Composable
fun GenreList(modifier: Modifier = Modifier) {
    val generos = listOf(
        "Rock",
        "Pop",
        "Jazz",
        "Hip Hop",
        "Electrónica",
        "Reggae",
        "Clásica",
        "Country",
        "Pop",
        "Jazz",
        "Hip Hop",
        "Electrónica",
        "Reggae",
        "Clásica",
        "Country"
    )

    LazyColumn(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        items(generos.chunked(2)) { rowItems ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                for (genre in rowItems) {
                    val imagenPrueba = painterResource(id = R.drawable.pordefecto)
                    GeneroCard(genre = genre, modifier = Modifier.weight(1f),imagenPrueba)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun GeneroCard(genre: String, modifier: Modifier = Modifier,imagePainter: Painter) {
    val randomColor by remember { mutableStateOf(generateRandomColor()) }
    Card(
        modifier = modifier
            .padding(10.dp)
            .height(90.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = randomColor,
            contentColor = Color.White,
        ),
        border = BorderStroke(4.dp, Color.Black)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = genre
                )
            }
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(horizontal = 5.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }
    }
}
// Función para generar un color aleatorio
private fun generateRandomColor(): Color {
    val random = Random()
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 1f
    )
}
