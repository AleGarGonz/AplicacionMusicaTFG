package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.AnadirListasComponentes.Body
import com.example.aplicacionmusicatfg.controladores.LoginController
@Composable
fun AnadirListaScreen(navController: NavController, loginController: LoginController){
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Body(modifier = Modifier.align(Alignment.Center), loginController,navController)
    }
}