package com.example.aplicacionmusicatfg.componentes.PerfilUsuarioScreenComponentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.UsuarioController
import com.example.aplicacionmusicatfg.modelos.Usuario


@Composable
fun Body(
    modifier: Modifier,
    logincontrol: LoginController,
    navController: NavController,
    usuarioController: UsuarioController
) {
    var usuario: Usuario? by remember { mutableStateOf(null) }

    val emailBuscado = logincontrol.getCurrentUser()?.email.toString()

    // Descargar y asignar el usuario al estado cuando esté disponible
    LaunchedEffect(key1 = emailBuscado) {
        usuarioController.descargarUsuario(emailBuscado) { usuarioDescargado ->
            usuario = usuarioDescargado
        }
    }

    // Renderizar la UI cuando el usuario esté disponible
    usuario?.let { usuario ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.pordefecto),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = usuario.nombre)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = usuario.email)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = usuario.biografia,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = usuario.generofav,
                color = Color.Gray
            )
        }
    }
}