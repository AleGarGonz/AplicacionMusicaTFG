package com.example.aplicacionmusicatfg.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.MiFooter
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.MiHeader
import com.example.aplicacionmusicatfg.componentes.ListasDeReproComponentes.Body

import com.example.aplicacionmusicatfg.controladores.LoginController

@Composable
fun ListaReproScreen(navController: NavController, loginController: LoginController) {
    var showDialog by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Body(modifier = Modifier.align(Alignment.Center), loginController,navController)
        MiHeader(
            Modifier.align(Alignment.TopEnd),
            loginController = loginController,
            showDialog = showDialog,
            onShowDialogChange = { showDialog = it },
            navController= navController
        )
        MiFooter(Modifier.align(Alignment.BottomCenter),navController)
    }
}