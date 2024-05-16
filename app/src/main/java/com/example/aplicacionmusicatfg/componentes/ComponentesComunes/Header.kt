package com.example.aplicacionmusicatfg.componentes.ComponentesComunes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.navigation.Rutas

@Composable
fun MiHeader(
    modifier: Modifier,
    loginController: LoginController,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    navController: NavController
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val currentDestinationId = navController.currentDestination?.id
        val generoDestinationId = navController.graph.findNode(Rutas.GenerosScreen.route)?.id
        val perfilDestinationId = navController.graph.findNode(Rutas.PerfilUsuario.route)?.id
        val listareproDestinationId = navController.graph.findNode(Rutas.ListaRepro.route)?.id
        if(currentDestinationId == generoDestinationId ) {
            Text(
                modifier = Modifier.padding(5.dp).padding(start = 6.dp),
                text = "Descubre",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Black
                )
            )
        }else{
            if(currentDestinationId == perfilDestinationId ) {
                Text(
                    modifier = Modifier.padding(5.dp).padding(start = 6.dp),
                    text = "Perfil",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.Black
                    )
                )
            }else{
                if(currentDestinationId == listareproDestinationId ) {
                    Text(
                        modifier = Modifier.padding(5.dp).padding(start = 6.dp),
                        text = "Listas de\nReproducción",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Black
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = { onShowDialogChange(true) },
            modifier = Modifier.padding(top = 5.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_logout_24),
                contentDescription = "Configuracion",
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
        }
    }

    CerrarSesionDialog(
        showDialog = showDialog,
        onDismissRequest = { onShowDialogChange(false) },
        loginController = loginController,
        navController =navController
    )
}

@Composable
fun CerrarSesionDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    loginController: LoginController,
    navController: NavController
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = "Cerrar Sesión") },
            text = {
                Text("¿Estás seguro de que deseas cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        loginController.signOut()
                        val currentDestinationId = navController.currentDestination?.id
                        val loginDestinationId = navController.graph.findNode(Rutas.Login.route)?.id
                        if (currentDestinationId != null && loginDestinationId != null) {
                            // Navegar a la pantalla de inicio de sesión, eliminando la pantalla actual del backstack
                            navController.navigate(Rutas.Login.route) {
                                popUpTo(currentDestinationId) {
                                    saveState = true
                                    inclusive = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        onDismissRequest()
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}