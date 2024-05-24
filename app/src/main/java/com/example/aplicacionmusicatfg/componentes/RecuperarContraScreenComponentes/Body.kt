package com.example.aplicacionmusicatfg.componentes.RecuperarContraScreenComponentes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.Email
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.ImageLogo
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.ParaContinuarText
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.emailText
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.isValidEmail
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.navigation.Rutas


@Composable
fun Body(modifier: Modifier,logincontrol: LoginController,navController: NavController) {
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var isLoginEnable by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = modifier.fillMaxSize()
        .background(color = Color.White)) {
        Spacer(modifier = Modifier.size(60.dp))
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        ResetPasstext(modifier=Modifier.align(Alignment.CenterHorizontally))
        ParaContinuarText(modifier=Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(20.dp))
        emailText(modifier= Modifier.padding(start=25.dp))
        Email(email) {
            email = it
            if (email.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(6.dp))
        Spacer(modifier = Modifier.size(16.dp))
        EnviarEmailButton(isLoginEnable,logincontrol,email,navController)
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun EnviarEmailButton(loginEnable: Boolean,viewModel: LoginController,email: String,navController: NavController) {
    val context = LocalContext.current
    Row(Modifier.padding(horizontal = 102.dp)){
        Button(
            onClick = {    viewModel.resetPasswordByEmail(email,
                onSuccess = {
                    Toast.makeText(context, "Se ha enviado un email a tu correo!", Toast.LENGTH_SHORT).show()
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
                },
                onError = { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            ) }, enabled = loginEnable,
            modifier = Modifier
                .width(180.dp)
                .padding(horizontal = 18.dp),
            colors = ButtonDefaults.buttonColors(
                disabledContentColor = Color.White,
                contentColor = Color.White,
                containerColor = Color(0xFF0B2FE6),
                disabledContainerColor = Color(0xFF00BCD4)

            )
        ) {
            Text(text = "Enviar Email")
        }
    }
}
@Composable
private fun ResetPasstext(modifier: Modifier) {
    Text(
        text = "Recuperar contraseña",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000000),
        modifier = modifier
    )
}