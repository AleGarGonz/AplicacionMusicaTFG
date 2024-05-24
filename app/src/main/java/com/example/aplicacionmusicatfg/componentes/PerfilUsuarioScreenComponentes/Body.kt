package com.example.aplicacionmusicatfg.componentes.PerfilUsuarioScreenComponentes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.biografiaText
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.emailText
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.generoFavText
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.UsuarioController
import com.example.aplicacionmusicatfg.controladores.getImagenStorage
import com.example.aplicacionmusicatfg.modelos.Usuario
import com.example.aplicacionmusicatfg.navigation.Rutas
import java.io.File


@Composable
fun Body(
    modifier: Modifier,
    logincontrol: LoginController,
    navController: NavController,
    usuarioController: UsuarioController
) {
    var usuario: Usuario? by remember { mutableStateOf(null) }

    val emailBuscado = logincontrol.getCurrentUser()?.email.toString()

    var ImagenFile: File? = null;

    var imagenPerfilVisible by rememberSaveable { mutableStateOf(false) }

    val context =LocalContext.current

    // Descargar y asignar el usuario al estado cuando esté disponible
    LaunchedEffect(key1 = emailBuscado) {
        usuarioController.descargarUsuario(emailBuscado) { usuarioDescargado ->
            usuario = usuarioDescargado
        }
    }

    usuario?.let { usuario ->
        if(usuario.fotoperfil != null && usuario.fotoperfil.isNotBlank() && usuario.fotoperfil.isNotEmpty() ){
            getImagenStorage(LocalContext.current, usuario!!.fotoperfil) { archivo, excepcion ->
                if (excepcion != null) {
                    println("Error al descargar archivos: ${excepcion.message}")
                } else {
                    ImagenFile = archivo
                }
            }
            if(!imagenPerfilVisible){
                LaunchedEffect(
                    validarImagenUsuario(ImagenFile,usuario)){
                    imagenPerfilVisible = true
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(140.dp)
                .background(
                    color = Color.Black
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    color = Color.Cyan,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 95.dp)
                .padding(start = 4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            if(validarImagenUsuario(ImagenFile,usuario)) {
                // Lee el archivo en un Bitmap
                val bitmap: Bitmap = BitmapFactory.decodeFile(ImagenFile!!.absolutePath)

                // Convierte el Bitmap en un ImageBitmap
                val imageBitmap = bitmap.asImageBitmap()
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.imagenperfilpordefecto),
                    contentDescription = "Descripción de la imagen",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            }
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(start = 14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = usuario.nombre,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = {  navController.navigate(route = Rutas.EditarPerfil.route) },
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .padding(end = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
                        ) {
                            Text(text = "Editar Perfil", color = Color.Black)
                        }
                    }
                    Spacer(modifier = modifier.height(12.dp))
                    emailText(modifier)
                    if(logincontrol.getCurrentUser()!!.isEmailVerified){
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = usuario.email,
                                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                                modifier = Modifier.padding(bottom = 8.dp),
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_check_24),
                                contentDescription = "Verificado",
                                tint = Color.Green,
                                modifier = Modifier.size(24.dp).padding(bottom = 6.dp) // Tamaño del icono
                            )
                        }
                    }else{
                        Button(
                            onClick = {
                                logincontrol.verificarEmailUsuario(
                                    onSuccess = {
                                        Toast.makeText(context, "Se ha enviado un email de verificación!", Toast.LENGTH_SHORT).show()
                                    },
                                    onError = { errorMessage ->
                                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "Verifica tu email",
                                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = modifier.height(12.dp))
                    biografiaText(modifier)
                    if(usuario.biografia.isEmpty()){
                        Text(
                            text = "Aún no sabemos nada sobre ti...",
                            modifier = Modifier.padding(end = 40.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
                        )
                    }else {
                        Text(
                            text = usuario.biografia,
                            modifier = Modifier.padding(end = 40.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = modifier.height(12.dp))
                    generoFavText(modifier)
                    if(usuario.generofav.isEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_music_note_24),
                                contentDescription = null,
                                tint = Color.Cyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "¿Cual es tu genero favorito?",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }else{
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_music_note_24),
                                contentDescription = null,
                                tint = Color.Cyan,
                                modifier = Modifier.size(16.dp) 
                            )
                            Text(
                                text = usuario.generofav,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
private fun validarImagenUsuario(ImagenFile: File?, usuario: Usuario): Boolean {
    return ImagenFile != null &&
            ImagenFile.isFile &&
            ImagenFile.exists() &&
            ImagenFile.length() > 0L &&
            usuario.fotoperfil != null &&
            usuario.fotoperfil.isNotBlank() &&
            usuario.fotoperfil.isNotEmpty()
}
