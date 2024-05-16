package com.example.aplicacionmusicatfg.componentes.ListaReproComponentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.ListaReproController
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.modelos.ListaReproduccion

val listasreprocontroller = ListaReproController()
/*// Crear una lista de canciones
val canciones = listOf(
    Cancion().apply {
        artista = "Artista 1"
        audio = "ruta/audio1.mp3"
        genero = "Género 1"
        imagen = "ruta/imagen1.jpg"
        titulo = "Canción 1"
    },
    Cancion().apply {
        artista = "Artista 2"
        audio = "ruta/audio2.mp3"
        genero = "Género 2"
        imagen = "ruta/imagen2.jpg"
        titulo = "Canción 2"
    }
)

// Crear una lista de reproducción y asignarle las canciones
val listaReproduccion = ListaReproduccion().apply {
    Listanombre = "Mi Lista de Reproducción"
    Canciones = canciones
}*/
@Composable
fun Body(
    modifier: Modifier,
    logincontrol: LoginController,
    navController: NavController,
) {
    val uid = logincontrol.getCurrentUser()?.uid.toString()
    var listasReproduccionDescargadas by remember { mutableStateOf(emptyList<ListaReproduccion>()) }
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
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top=80.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.End).padding(end = 26.dp)
        ) {
            BotonNuevaLista(onClick = {
                // Por hacer el cambio
            })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            color = Color.White,
            thickness = 2.dp,
            modifier = Modifier.width(360.dp).padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Descargar todas las listas de reproducción
        LaunchedEffect(Unit) {
            listasreprocontroller.descargarTodasLasListasReproduccion(uid) { listas ->
                listasReproduccionDescargadas = listas
            }
        }
            ListaReproduccionCards(listas = listasReproduccionDescargadas,modifier)
    }
}

@Composable
fun ListaReproduccionCards(listas: List<ListaReproduccion>,modifier: Modifier) {
    LazyColumn(modifier = modifier
        .fillMaxSize()) {
        items(listas) { lista ->
            ListaReproduccionItem(nombreLista = lista.Listanombre)
        }
    }
}
@Composable
fun ListaReproduccionItem(nombreLista: String) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .height(70.dp)
            .padding(start = 12.dp, end = 60.dp)
            .padding(vertical = 6.dp)
            .background(Color.Transparent)
            .clickable { /* por hacer */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = nombreLista,
                fontSize = 21.sp,
                textAlign = TextAlign.Start,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun BotonNuevaLista(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Cyan),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nueva Lista",
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
                modifier = Modifier.size(24.dp)
            )
        }
    }
}