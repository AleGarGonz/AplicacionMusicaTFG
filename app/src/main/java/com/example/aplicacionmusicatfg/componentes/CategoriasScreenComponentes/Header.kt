package com.example.aplicacionmusicatfg.componentes.CategoriasScreenComponentes

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
        Text(
            modifier = Modifier.padding(5.dp).padding(start = 6.dp),
            text = "Descubre",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        )
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

    // Llamamos al diálogo de cierre de sesión y pasamos los parámetros necesarios
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
                        // Navegar a la pantalla de inicio de sesión
                        // Obtener el ID de la pantalla actual
                        val currentDestinationId = navController.currentDestination?.id
                        // Obtener el ID de la pantalla de inicio de sesión
                        val loginDestinationId = navController.graph.findNode(Rutas.Login.route)?.id
                        // Verificar si ambos IDs son válidos
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