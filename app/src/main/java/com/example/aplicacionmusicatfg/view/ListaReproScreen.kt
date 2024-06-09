package com.example.aplicacionmusicatfg.view

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.MiFooter
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.MiHeader
import com.example.aplicacionmusicatfg.componentes.ListasDeReproScreenComponentes.Body

import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.navigation.Rutas

@Composable
fun ListaReproScreen(navController: NavController, loginController: LoginController) {
    var showDialog by remember { mutableStateOf(false) }
    val currentDestinationId = navController.currentDestination?.id
    val lifecycleOwner = LocalLifecycleOwner.current
    val onBackPressedDispatcher = (LocalContext.current as ComponentActivity).onBackPressedDispatcher
    //Vuelve a la pantalla de los generos(Principal)
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigate(Rutas.GenerosScreen.route){
                    if (currentDestinationId != null) {
                        popUpTo(currentDestinationId) {
                            saveState = true
                            inclusive = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
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
        onBackPressedDispatcher.addCallback(lifecycleOwner, backCallback)
    }
}