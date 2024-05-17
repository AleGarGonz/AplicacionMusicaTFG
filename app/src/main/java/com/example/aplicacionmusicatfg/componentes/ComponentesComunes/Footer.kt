package com.example.aplicacionmusicatfg.componentes.ComponentesComunes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.navigation.Rutas

//La pantalla se deberia llamar genero y no categorias
@Composable
fun MiFooter(modifier: Modifier = Modifier,navController:NavController) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .padding(horizontal = 0.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Black.copy(alpha = 0.6f))
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FooterIconButton(
                drawableId = R.drawable.baseline_list_24,
                contentDescription = "Inicio",
                onClick = {
                    val currentDestinationId = navController.currentDestination?.id
                    val listareproDestinationId = navController.graph.findNode(Rutas.ListaRepro.route)?.id
                    if (currentDestinationId != null && listareproDestinationId != null) {
                        navController.navigate(Rutas.ListaRepro.route) {
                            popUpTo(currentDestinationId) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
            FooterIconButton(
                drawableId = R.drawable.baseline_search_24,
                contentDescription = "Buscar",
                onClick = {
                    val currentDestinationId = navController.currentDestination?.id
                    val generoDestinationId = navController.graph.findNode(Rutas.GenerosScreen.route)?.id
                    if (currentDestinationId != null && generoDestinationId != null) {
                        navController.navigate(Rutas.GenerosScreen.route) {
                            popUpTo(currentDestinationId) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
            FooterIconButton(
                drawableId = R.drawable.baseline_person_24,
                contentDescription = "Configuración",
                onClick = {
                    val currentDestinationId = navController.currentDestination?.id
                    val perfilDestinationId = navController.graph.findNode(Rutas.PerfilUsuario.route)?.id
                    if (currentDestinationId != null && perfilDestinationId != null) {
                        navController.navigate(Rutas.PerfilUsuario.route) {
                            popUpTo(currentDestinationId) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun FooterIconButton(drawableId: Int, contentDescription: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.height(40.dp),
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = Color.White
        )
    }
}
