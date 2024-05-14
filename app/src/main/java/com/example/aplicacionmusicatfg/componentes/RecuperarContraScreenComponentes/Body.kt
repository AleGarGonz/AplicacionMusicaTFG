package com.example.aplicacionmusicatfg.componentes.RecuperarContraScreenComponentes

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
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
        LogIntext(modifier=Modifier.align(Alignment.CenterHorizontally))
        AddsManagertext(modifier=Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(20.dp))
        UsernameText(modifier= Modifier)
        Email(email) {
            email = it
            if (email.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(6.dp))
        Spacer(modifier = Modifier.size(16.dp))
        LoginButton(isLoginEnable,logincontrol,email,navController)
        Spacer(modifier = Modifier.size(16.dp))
    }
}

@Composable
fun LoginButton(loginEnable: Boolean,viewModel: LoginController,email: String,navController: NavController) {
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
            Text(text = "Send Email")
        }
    }
}
@Composable
fun UsernameText(modifier: Modifier) {
    Text(
        text = "\t\t\t\tEmail",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
        modifier = modifier
    )
}
@Composable
fun AddsManagertext(modifier: Modifier) {
    Text(
        text = "to continue",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
        modifier = modifier
    )
}
@Composable
fun LogIntext(modifier: Modifier) {
    Text(
        text = "Reset Password",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000000),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        placeholder = { Text(text = "Email") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Gray,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            containerColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}


@Composable
fun ImageLogo(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logoappmusicatfg),
        contentDescription = "logo",
        modifier = modifier
            .size(150.dp)
            .clip(CircleShape)
    )
}


fun isValidEmail(email: String): Boolean {
    return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
}