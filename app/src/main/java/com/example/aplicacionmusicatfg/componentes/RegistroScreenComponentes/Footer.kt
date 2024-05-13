package com.example.aplicacionmusicatfg.componentes.RegistroScreenComponentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.navigation.Rutas


@Composable
fun Footer(modifier: Modifier,navController: NavController) {
    Column(modifier = modifier.fillMaxWidth().background(Color(0xFFB8DAD9))) {

        Divider(
            Modifier
                .background(Color(0xFFF9F9F9))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(24.dp))
        InicioSesion(){
            val currentDestinationId = navController.currentDestination?.id
            val loginDestinationId = navController.graph.findNode(Rutas.Login.route)?.id
            if (currentDestinationId != null && loginDestinationId != null) {
                navController.navigate(Rutas.Login.route) {
                    popUpTo(currentDestinationId) {
                        saveState = true
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        Spacer(modifier = Modifier.size(24.dp))

    }

}
@Composable
fun InicioSesion(onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        SignUpText(onClick)
    }
}

@Composable
fun SignUpText(onClick: () -> Unit) {
    Text(
        text = "¿Tienes cuenta? Inicia Sesión",
        modifier = Modifier.clickable { onClick() }.padding(horizontal = 8.dp),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000000)
    )
}