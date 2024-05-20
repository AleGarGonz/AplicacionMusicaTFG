package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.ListaComponentes.Body
import com.example.aplicacionmusicatfg.controladores.LoginController

@Composable
fun ListaScreen(navController: NavController, loginController: LoginController,ListaID: String) {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Body(navController =navController,loginController,ListaID)
    }
}