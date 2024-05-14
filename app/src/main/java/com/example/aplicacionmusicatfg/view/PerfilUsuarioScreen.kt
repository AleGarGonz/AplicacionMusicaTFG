package com.example.aplicacionmusicatfg.view


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.MiFooter
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.MiHeader
import com.example.aplicacionmusicatfg.componentes.PerfilUsuarioScreenComponentes.Body
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.UsuarioController

@Composable
fun PerfilUsuarioScreen(navController: NavController, loginController: LoginController,usuarioController: UsuarioController) {
    var showDialog by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)) {
        Body(Modifier.align(Alignment.Center), loginController,navController,usuarioController)
        MiHeader(
            modifier = Modifier.align(Alignment.TopEnd),
            loginController = loginController,
            showDialog = showDialog,
            onShowDialogChange = { showDialog = it },
            navController = navController
        )
        MiFooter(Modifier.align(Alignment.BottomCenter),navController = navController)
    }
}