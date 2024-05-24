package com.example.aplicacionmusicatfg.componentes.RegistroScreenComponentes


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.nombreText
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.UsuarioController
import com.example.aplicacionmusicatfg.modelos.Usuario
import com.example.aplicacionmusicatfg.navigation.Rutas


@Composable
fun Body(modifier: Modifier,logincontrol: LoginController,navController: NavController,usuarioController: UsuarioController) {
    var email by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var nombre by rememberSaveable {
        mutableStateOf("")
    }
    var isLoginEnable by rememberSaveable {
        mutableStateOf(false)
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(color = Color.White)) {
        Spacer(modifier = Modifier.size(60.dp))
        ImageLogo(Modifier.align(Alignment.CenterHorizontally))
        SignUptext(modifier=Modifier.align(Alignment.CenterHorizontally))
        ParaContinuarText(modifier=Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.size(20.dp))
        emailText(modifier= Modifier.padding(start=25.dp))
        Email(email) {
            email = it
            if (password.length > 0 && email.length > 0 && nombre.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(6.dp))
        PasswordText(modifier=Modifier.padding(start=25.dp))
        Password(password) {
            password = it
            if (password.length > 0 && email.length > 0 && nombre.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(6.dp))
        nombreText(modifier=Modifier.padding(start = 25.dp))
        Nombre(nombre){
            nombre = it
            if (password.length > 0 && email.length > 0 && nombre.length > 0 && isValidEmail(email))
                isLoginEnable = true
            else
                isLoginEnable = false
        }
        Spacer(modifier = Modifier.size(16.dp))
        RegisterButton(isLoginEnable,logincontrol,email,password,nombre,navController,usuarioController)
        Spacer(modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.size(16.dp))

    }
}

@Composable
private fun RegisterButton(loginEnable: Boolean,logincontrol: LoginController,email: String,password: String,nombre:String,navController: NavController,usuarioController: UsuarioController) {
    val context = LocalContext.current
    Row(Modifier.padding(horizontal = 102.dp)){
        Button(
            onClick = {    logincontrol.createUserWithEmailAndPassword(email, password,
                onSuccess = {
                    Toast.makeText(context, "Cuenta creada con exito!", Toast.LENGTH_SHORT).show()

                    var nuevoUsuario = Usuario()
                    nuevoUsuario.biografia =""
                    nuevoUsuario.email = email
                    nuevoUsuario.fotoperfil =""
                    nuevoUsuario.generofav =""
                    nuevoUsuario.nombre = nombre

                    usuarioController.subirUsuario(nuevoUsuario,
                        onSuccess = {
                            Toast.makeText(context, "Usuario creado con exito!", Toast.LENGTH_SHORT).show()
                            navController.navigate(route = Rutas.GenerosScreen.route)
                        },
                        onError = { errorMessage ->
                            println("Error al subir el usuario: $errorMessage")
                        }
                    )
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
            Text(text = "Sign Up")
        }
    }
}
@Composable
private fun SignUptext(modifier: Modifier) {
    Text(
        text = "Registrate",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000000),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Nombre(nombre: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = nombre,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        placeholder = { Text(text = "Nombre") },
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