package com.example.aplicacionmusicatfg

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.Password
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.PasswordText
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.emailText
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.isValidEmail
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.navigation.Rutas


@Composable
fun Body(modifier: Modifier,logincontrol: LoginController,navController: NavController) {
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var isLoginEnable by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.size(60.dp))
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        LogIntext(modifier=Modifier.align(Alignment.CenterHorizontally))
        ParaContinuarText(modifier=Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(20.dp))
        emailText(modifier= Modifier.padding(start=25.dp))
        Email(email) {
            email = it
            if (password.length > 0 && email.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(6.dp))
        PasswordText(modifier=Modifier.padding(start=25.dp))
        Password(password) {
            password = it
            if (password.length > 0 && email.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        ForgotPassword(Modifier.align(Alignment.CenterHorizontally).padding(start=150.dp)){
            navController.navigate(route = Rutas.RecuperarContra.route)
        }
        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(isLoginEnable,logincontrol,email,password,navController)
        Spacer(modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.size(16.dp))

    }
}

@Composable
private fun LoginButton(loginEnable: Boolean,logincontrol: LoginController,email: String,password: String,navController: NavController) {
    val context = LocalContext.current
    Row(Modifier.padding(horizontal = 102.dp)){
        Button(
            onClick = { logincontrol.signInWithEmailAndPassword(email, password,
                onSuccess = {
                    Toast.makeText(context, "Inicio de sesion exitoso!", Toast.LENGTH_SHORT).show()
                    val currentDestinationId = navController.currentDestination?.id
                    navController.navigate(route = Rutas.GenerosScreen.route){
                        if (currentDestinationId != null) {
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
            Text(text = "Log In")
        }
    }
}

@Composable
private fun ForgotPassword(modifier: Modifier, onClick: () -> Unit) {
    Text(
        text = "¿Olvidaste tu contraseña?",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFFB5B5B5),
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    )
}
@Composable
private fun LogIntext(modifier: Modifier) {
    Text(
        text = "Inicia Sesión",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000000),
        modifier = modifier
    )
}