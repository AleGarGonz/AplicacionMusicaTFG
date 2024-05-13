package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.RegistroScreenComponentes.Body
import com.example.aplicacionmusicatfg.componentes.RegistroScreenComponentes.Footer
import com.example.aplicacionmusicatfg.controladores.LoginController

@Composable
fun RegistroScreen(navController: NavController, loginController: LoginController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)) {

        Body(Modifier.align(Alignment.Center), loginController,navController)
        Footer(Modifier.align(Alignment.BottomCenter),navController)

    }
}