package com.example.aplicacionmusicatfg.componentes.AnadirListasScreenComponentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.ListaReproController
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.modelos.ListaReproduccion
import com.example.aplicacionmusicatfg.navigation.Rutas
import kotlinx.coroutines.delay

private val listasreprocontroller = ListaReproController()
@Composable
fun Body(
    modifier: Modifier,
    logincontrol: LoginController,
    navController: NavController,
) {
    val uid = logincontrol.getCurrentUser()?.uid.toString()
    var listasReproduccionDescargadas by remember { mutableStateOf(emptyList<ListaReproduccion>()) }
    var actualizarlista  by remember { mutableStateOf(true) }
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
    Text(
        text = "Listas Públicas",
        modifier = Modifier
            .padding(start = 6.dp)
            .padding(top = 12.dp)
            .padding(horizontal = 12.dp),
        style = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color =Color.Black
        )
    )
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top=80.dp).padding(bottom = 44.dp)
    ) {
        Divider(
            color = Color.White,
            thickness = 3.dp,
            modifier = Modifier.width(360.dp).padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Descargar todas las listas de reproducción
        LaunchedEffect(actualizarlista) {
            if (listasReproduccionDescargadas.isEmpty()) {
               listasreprocontroller.descargarTodasLasListasReproduccionDeTodosLosUsuarios { listas ->
                    listasreprocontroller.descargarTodasLasListasReproduccion(uid) { listasDeUID ->
                        val listasFiltradas = listas.filterNot { lista ->
                            listasDeUID.any { it.id == lista.id }
                        }
                        listasReproduccionDescargadas = listasFiltradas
                        actualizarlista = false
                    }
                }
            } else {
                delay(2000)
                listasreprocontroller.descargarTodasLasListasReproduccionDeTodosLosUsuarios { listas ->
                    listasreprocontroller.descargarTodasLasListasReproduccion(uid) { listasDeUID ->
                        val listasFiltradas = listas.filterNot { lista ->
                            listasDeUID.any { it.id == lista.id }
                        }
                        listasReproduccionDescargadas = listasFiltradas
                        actualizarlista = false
                    }
                }
            }
        }
        ListaReproduccionCards(listas = listasReproduccionDescargadas, modifier,navController,uid) { listaId ->
            listasReproduccionDescargadas = listasReproduccionDescargadas.filterNot { it.id == listaId }
        }
    }
}
//LazyColumn que contiene todos los items(cards) de las listas de reproducción
@Composable
private fun ListaReproduccionCards(
    listas: List<ListaReproduccion>,
    modifier: Modifier,
    navController:NavController,
    uid:String,
    onDelete: (String) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(listas) { lista ->
            ListaReproduccionItem(lista = lista,uid,navController)
        }
    }
}
//Items(cards) que muestra el lazycolumn de cada lista de reproducción
@Composable
private fun ListaReproduccionItem(
    lista: ListaReproduccion,
    uid: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .height(70.dp)
            .padding(start = 12.dp, end = 6.dp)
            .padding(vertical = 6.dp)
            .background(Color.Transparent)
            .clickable {
                navController.navigate(
                    route = Rutas.Lista.createRoute(
                        lista.id
                    )
                ) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            //Si se añade la lista desactivo el boton para que no pueda causar problemas
            var isAdded by remember { mutableStateOf(false) }
            Text(
                text = lista.Listanombre,
                fontSize = 21.sp,
                textAlign = TextAlign.Start,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            )
            IconButton(onClick = {
                listasreprocontroller.subirListaReproduccion(lista,uid,lista.id)
                isAdded = true
            },
                enabled = !isAdded) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "AnadirLista",
                    tint = if (!isAdded) Color.Black else Color.LightGray
                )
            }
        }
    }
}