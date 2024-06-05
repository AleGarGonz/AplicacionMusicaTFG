package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.EditarPerfilScreenComponentes.Body

import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.UsuarioController

@Composable
fun EditarPerfilScreen(navController: NavController, loginController: LoginController, usuarioController: UsuarioController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)) {
        Body(Modifier.align(Alignment.Center), loginController,navController,usuarioController)
    }
}