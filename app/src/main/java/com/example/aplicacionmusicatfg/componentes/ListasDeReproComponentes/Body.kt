package com.example.aplicacionmusicatfg.componentes.ListasDeReproComponentes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.aplicacionmusicatfg.navigation.Rutas
import kotlinx.coroutines.delay

val listasreprocontroller = ListaReproController() //Igual estaria bien ponerlo fuera no lo se... lo detectan todas las ventanas que se abren de esta no se porqie
@Composable
fun Body(
    modifier: Modifier,
    logincontrol: LoginController,
    navController: NavController,
) {
    val uid = logincontrol.getCurrentUser()?.uid.toString()
    var listasReproduccionDescargadas by remember { mutableStateOf(emptyList<ListaReproduccion>()) }
    var showDialog by remember { mutableStateOf(false) }
    var actualizarlista  by remember { mutableStateOf(true) }
    var context = LocalContext.current
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
        .padding(top=80.dp).padding(bottom = 44.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.End).padding(end = 26.dp)
        ) {
            BotonNuevaLista(onClick = {
                showDialog = true
            })
            BotonAnadirLista(onClick = {
                navController.navigate(
                    route = Rutas.AnadirLista.route)
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
        LaunchedEffect(actualizarlista) {
            if(listasReproduccionDescargadas.isEmpty()){
                listasreprocontroller.descargarTodasLasListasReproduccion(uid) { listas ->
                    listasReproduccionDescargadas = listas
                    actualizarlista = false
                }
            }else{
                delay(2000)
                listasreprocontroller.descargarTodasLasListasReproduccion(uid) { listas ->
                    listasReproduccionDescargadas = listas
                    actualizarlista = false
                }
            }
        }
        ListaReproduccionCards(listas = listasReproduccionDescargadas, modifier,navController) { listaId ->
            listasreprocontroller.borrarListaReproduccion(uid, listaId)
            listasReproduccionDescargadas = listasReproduccionDescargadas.filterNot { it.id == listaId }
        }
    }
    if (showDialog) {
        CrearListaDialog(
            onDismiss = { showDialog = false },
            onCreate = { nombreLista ->
                val nuevaLista = ListaReproduccion()
                nuevaLista.Listanombre = nombreLista
                listasreprocontroller.subirListaReproduccion(nuevaLista, uid,nuevaLista.id)
                showDialog = false
                actualizarlista = true
                Toast.makeText(context, "Lista creada!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun ListaReproduccionCards(
    listas: List<ListaReproduccion>,
    modifier: Modifier,
    navController:NavController,
    onDelete: (String) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(listas) { lista ->
            ListaReproduccionItem(nombreLista = lista.Listanombre, listaId = lista.id,navController, onDelete = onDelete)
        }
    }
}
@Composable
fun ListaReproduccionItem(
    nombreLista: String,
    listaId: String,
    navController: NavController,
    onDelete: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

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
                        listaId
                    )
                ) },
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
                    .weight(1f)
            )
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            containerColor = Color.Black,
            onDismissRequest = { showDialog = false },
            title = { Text(text = "¿Estás seguro que deseas borrar la lista?", fontSize = 20.sp) },
            confirmButton = {
                Button(onClick = {
                    onDelete(listaId)
                    showDialog = false
                },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)) {
                    Text("Sí",color = Color.Black)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("No",color = Color.White)
                }
            }
        )
    }
}

@Composable
private fun BotonNuevaLista(onClick: () -> Unit) {
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
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
    }
}
@Composable
private fun BotonAnadirLista(onClick: () -> Unit) {
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
                text = "Añadir Lista",
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
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun CrearListaDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var nombreLista by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Nueva Lista de Reproducción", color = Color.White) },
        text = {
            Column {
                Text(text = "Ingrese el nombre de la nueva lista:",color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = nombreLista,
                    onValueChange = { nombreLista = it },
                    placeholder = { Text(text = "Nombre de la lista") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onCreate(nombreLista) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                enabled = nombreLista.isNotBlank()
            ) {
                Text("Crear",color = Color.Black)
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("Cancelar",color = Color.White)
            }
        }
    )
}