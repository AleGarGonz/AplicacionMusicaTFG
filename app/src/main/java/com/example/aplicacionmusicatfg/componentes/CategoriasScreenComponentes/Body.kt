package com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.getImagenStorage
import com.example.aplicacionmusicatfg.controladores.getListaGenerosRealtime
import com.example.aplicacionmusicatfg.modelos.Genero
import com.example.aplicacionmusicatfg.navigation.Rutas
import kotlinx.coroutines.delay
import java.io.File
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiBody(modifier: Modifier,navController: NavController) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Cyan.copy(alpha = 0.9f), Color.Black),
                    )
                )
        ) {
            Button(
                onClick = { navController.navigate(route = Rutas.BusquedaScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 70.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black // Color del contenido (ícono y texto)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_search_24),
                        contentDescription = "Buscar"
                    )
                    Text(
                        text = "Canciones",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.weight(1f) // Esto asegura que el Text ocupe todo el espacio disponible
                    )
                }
            }
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

            GenreList(modifier = Modifier.fillMaxWidth(), navController)
        }
}


@Composable
fun GenreList(modifier: Modifier = Modifier,navController: NavController) {
    var listaGeneros by remember { mutableStateOf(emptyList<Genero>()) }
    var isLoading by remember { mutableStateOf(true) }
    var ImagenFile: File? = null;
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        // Simular carga durante 2 segundos
        LaunchedEffect(Unit) {
            delay(300)
            isLoading = false
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(90.dp), // Tamaño personalizado
                color = Color.Blue
            )
        }else{
            getListaGenerosRealtime { generos ->
                listaGeneros = generos
            }
            LazyColumn(
                modifier = modifier.padding(vertical = 8.dp)
            ) {
                items(listaGeneros.chunked(2)) { rowItems ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        for (genre in rowItems) {
                            getImagenStorage(LocalContext.current,genre.imagen) { archivo, excepcion ->
                                if (excepcion != null) {
                                    println("Error al descargar archivos: ${excepcion.message}")
                                } else {
                                    if (archivo?.absoluteFile!!.name.contains(genre.imagen)) {
                                        ImagenFile = archivo
                                    }
                                }
                            }
                            GeneroCard(genero = genre, modifier = Modifier.weight(1f),ImagenFile,navController)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun GeneroCard(genero:Genero, modifier: Modifier = Modifier, imagen: File?,navController: NavController) {
    val randomColor by remember { mutableStateOf(generateRandomColor()) }
    fun isDark(color: Color): Boolean {
        // Convierte el color a escala de grises utilizando el método luminance
        val luminance = 0.2126f * color.red + 0.7152f * color.green + 0.0722f * color.blue
        // Determina si el color es oscuro o claro basado en su luminancia
        return luminance < 0.5f
    }

// Dentro de tu función de composición o en otro lugar donde tengas acceso al color del Card
    val textColor = if (isDark(randomColor)) Color.White else Color.Black
    var showDialog by remember { mutableStateOf(false) }

// Lógica para mostrar el diálogo cuando showDialog sea true
    if (showDialog) {
        InformacionDialog(
            texto = genero.descripcion,
            onDismissRequest = { showDialog = false } // Establece showDialog en false para cerrar el diálogo
        )
    }
    Card(
        modifier = modifier
            .padding(start = 15.dp)
            .height(150.dp) // Aumentar la altura para dar más espacio al texto
            .fillMaxWidth()
            .clickable { navController.navigate(Rutas.BusquedaScreen.createRoute(genero.nombre)) },
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = randomColor,
            contentColor = Color.White,
        ),
        border = BorderStroke(4.dp, Color.Black)
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = genero.nombre,
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f) // Utiliza el 100% del ancho disponible
                        .padding(vertical = 8.dp)
                )
                IconButton(
                    onClick = { showDialog = true }
                ) {
                    Icon(
                        painter =  painterResource(id = R.drawable.baseline_info_24),
                        contentDescription = "Información",
                        tint = textColor // Usa el mismo color que el texto
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp)) // Añadir un espacio entre el texto y la imagen

            if(imagen != null){
                // Lee el archivo en un Bitmap
                val bitmap: Bitmap = BitmapFactory.decodeFile(imagen?.absolutePath)

                // Convierte el Bitmap en un ImageBitmap
                val imageBitmap = bitmap.asImageBitmap()
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Descripción de la imagen",
                    modifier = Modifier.height(240.dp).fillMaxWidth()
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.pordefecto),
                    contentDescription = "Descripción de la imagen",
                    modifier = Modifier.height(240.dp).width(240.dp)
                )
            }
        }
    }
}

@Composable
fun InformacionDialog(texto: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Información",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(texto)
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Aceptar")
            }
        }
    )
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
