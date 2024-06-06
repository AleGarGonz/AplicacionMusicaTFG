package com.example.aplicacionmusicatfg.componentes.GenerosScreenComponentes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.aplicacionmusicatfg.controladores.GeneroController
import com.example.aplicacionmusicatfg.controladores.ImagenController
import com.example.aplicacionmusicatfg.modelos.Genero
import com.example.aplicacionmusicatfg.navigation.Rutas
import kotlinx.coroutines.delay
import java.io.File
import java.util.Random

@SuppressLint("UnrememberedMutableState")
@Composable
fun MiBody(modifier: Modifier,navController: NavController) {
    val generoController = GeneroController()
    var listaGeneros by remember { mutableStateOf(emptyList<Genero>()) }
    var archivosImagenes = remember { mutableStateListOf<File>() }
    val imagenController = ImagenController()
    var visible by rememberSaveable { mutableStateOf(false) }
    var context = LocalContext.current
    //Descargo la lista de generos
    if(listaGeneros.isEmpty()){
        generoController.getListaGenerosRealtime { generos ->
            listaGeneros = generos
            for (genre in listaGeneros) {
                //Descargo sus respectivas imagenes
                imagenController.getImagenStorage(
                    context,
                    genre.imagen
                ) { archivo, excepcion ->
                    if (excepcion != null) {
                        println("Error al descargar archivos: ${excepcion.message}")
                    } else {
                        val nombreImagen = genre.imagen?.substringBeforeLast(".")
                        if (archivo?.absoluteFile!!.name.contains(nombreImagen.toString())) {
                            archivosImagenes.add(archivo)
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(listaGeneros.size == archivosImagenes.size){
        if(!visible){
            delay(2000)
        }
        visible = true
    }
    if(visible){
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Cyan.copy(alpha = 0.9f), Color.Black),
                    )
                )
        ) {
            buscarButton(navController)
            explogentext(
                modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 12.dp))
            GeneroList(modifier = Modifier.fillMaxWidth(), navController,listaGeneros,archivosImagenes)
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Cyan.copy(alpha = 0.9f), Color.Black)
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .padding(60.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(150.dp),
                    color = Color.Blue,
                    strokeWidth = 8.dp
                )
            }
        }
    }
}


@Composable
private fun GeneroList(modifier: Modifier,navController: NavController,listaGeneros: List<Genero>,archivosImagenes:List<File>) {
    var ImagenFile: File? = null
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = modifier.padding(vertical = 8.dp)
        ) {
            items(listaGeneros.chunked(2)) { rowItems ->
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (genre in rowItems) {
                        val nombreImagen = genre.imagen?.substringBeforeLast(".")
                        for(img in archivosImagenes){
                            if (img?.absoluteFile!!.name.contains(nombreImagen.toString())) {
                                ImagenFile = img
                            }
                        }
                        GeneroCard(
                            genero = genre,
                            modifier = Modifier.weight(1f),
                            ImagenFile,
                            navController
                        )
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
    val textColor = if (isDark(randomColor)) Color.White else Color.Black
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        InformacionDialog(
            texto = genero.descripcion,
            onDismissRequest = { showDialog = false }
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
        border = BorderStroke(4.dp, Color.Cyan)
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
                //Dependiendo de si la imagen se ha descargado o no mostrara la imagen o una por defecto
                if (validarImagenGenero(imagen)) {
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
        containerColor = Color.Black,
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Información",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Text(texto,fontSize = 16.sp,color = Color.Gray)
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
            ) {
                Text("OK",color = Color.Black)
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
private fun isDark(color: Color): Boolean {
    val luminance = 0.2126f * color.red + 0.7152f * color.green + 0.0722f * color.blue
    return luminance < 0.5f
}

private fun validarImagenGenero(ImagenFile: File?): Boolean {
    return ImagenFile != null &&
            ImagenFile.isFile &&
            ImagenFile.exists() &&
            ImagenFile.length() > 0L
}

@Composable
private fun buscarButton(navController: NavController) {
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
}
@Composable
private fun explogentext(modifier: Modifier){
    Text(
        text = "Explorar géneros",
        modifier = modifier,
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color=Color.Black
        )
    )
}