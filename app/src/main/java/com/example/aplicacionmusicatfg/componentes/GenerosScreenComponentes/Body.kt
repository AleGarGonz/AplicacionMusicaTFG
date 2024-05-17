package com.example.aplicacionmusicatfg.componentes.GenerosScreenComponentes

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
                    contentColor = Color.Black
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
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Text(
                text = "Explorar géneros",
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 12.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color=Color.Black
                )
            )

            GeneroList(modifier = Modifier.fillMaxWidth(), navController)
        }
}


@Composable
private fun GeneroList(modifier: Modifier = Modifier,navController: NavController) {
    var listaGeneros by remember { mutableStateOf(emptyList<Genero>()) }
    var ImagenFile: File? = null;
    var cardVisible by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
            getListaGenerosRealtime { generos ->
                listaGeneros = generos
            }
            LazyColumn(
                modifier = modifier.padding(vertical = 8.dp)
            ) {
                items(listaGeneros.chunked(2)) { rowItems ->
                    Row(
                        Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (genre in rowItems) {
                            getImagenStorage(
                                LocalContext.current,
                                genre.imagen
                            ) { archivo, excepcion ->
                                if (excepcion != null) {
                                    println("Error al descargar archivos: ${excepcion.message}")
                                } else {
                                    val nombreImagen = genre.imagen?.substringBeforeLast(".")
                                    if (archivo?.absoluteFile!!.name.contains(nombreImagen.toString())) {
                                        ImagenFile = archivo
                                    }
                                }
                            }
                            if(!cardVisible){
                                LaunchedEffect(validarImagenGenero(ImagenFile,genre)){
                                    cardVisible = true
                                }
                            }
                            if(cardVisible){
                                GeneroCard(
                                    genero = genre,
                                    modifier = Modifier.weight(1f),
                                    ImagenFile,
                                    navController
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
    }
}

@Composable
private fun GeneroCard(genero:Genero, modifier: Modifier = Modifier, imagen: File?,navController: NavController) {
    val randomColor by remember { mutableStateOf(generateRandomColor()) }
    fun isDark(color: Color): Boolean {
        val luminance = 0.2126f * color.red + 0.7152f * color.green + 0.0722f * color.blue
        return luminance < 0.5f
    }
    val textColor = if (isDark(randomColor)) Color.White else Color.Black
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        InformacionDialog(
            texto = genero.descripcion,
            onDismissRequest = { showDialog = false } // Establece showDialog en false para cerrar el diálogo
        )
    }
    Card(
        modifier = modifier
            .padding(horizontal = 5.dp)
            .height(130.dp)
            .clickable { navController.navigate(Rutas.BusquedaScreen.createRoute(genero.nombre)) },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = randomColor,
        ),
        border = BorderStroke(4.dp, Color.Black)
    ) {
        Column(
            modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = genero.nombre,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter =  painterResource(id = R.drawable.baseline_info_24),
                        contentDescription = "Información",
                        tint = textColor
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                if (validarImagenGenero(imagen,genero)) {
                    // Lee el archivo en un Bitmap
                    val bitmap: Bitmap = BitmapFactory.decodeFile(imagen?.absolutePath)

                    // Convierte el Bitmap en un ImageBitmap
                    val imageBitmap = bitmap.asImageBitmap()
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Descripción de la imagen",
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(1f),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.pordefecto),
                        contentDescription = "Descripción de la imagen",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2f),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

@Composable
private fun InformacionDialog(texto: String, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Información",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(texto,fontSize = 16.sp )
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("OK")
            }
        }
    )
}
private fun generateRandomColor(): Color {
    val random = Random()
    return Color(
        red = random.nextFloat(),
        green = random.nextFloat(),
        blue = random.nextFloat(),
        alpha = 1f
    )
}

private fun validarImagenGenero(ImagenFile: File?, genero: Genero): Boolean {
    return ImagenFile != null &&
            ImagenFile.isFile &&
            ImagenFile.exists() &&
            ImagenFile.length() > 0L &&
            genero.imagen != null &&
            genero.imagen.isNotBlank() &&
            genero.imagen.isNotEmpty()
}