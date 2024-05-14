package com.example.aplicacionmusicatfg.componentes.PerfilUsuarioScreenComponentes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.asImageBitmap
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
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.UsuarioController
import com.example.aplicacionmusicatfg.controladores.getImagenStorage
import com.example.aplicacionmusicatfg.modelos.Usuario
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

    // Descargar y asignar el usuario al estado cuando esté disponible
    LaunchedEffect(key1 = emailBuscado) {
        usuarioController.descargarUsuario(emailBuscado) { usuarioDescargado ->
            usuario = usuarioDescargado
        }
    }

    usuario?.let { usuario ->
        getImagenStorage(LocalContext.current, usuario!!.fotoperfil) { archivo, excepcion ->
            if (excepcion != null) {
                println("Error al descargar archivos: ${excepcion.message}")
            } else {
                ImagenFile = archivo
            }
        }

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
                .padding(top = 70.dp).padding(start = 4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            if(ImagenFile != null) {
                // Lee el archivo en un Bitmap
                val bitmap: Bitmap = BitmapFactory.decodeFile(ImagenFile!!.absolutePath)

                // Convierte el Bitmap en un ImageBitmap
                val imageBitmap = bitmap.asImageBitmap()
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.imagenperfilpordefecto),
                    contentDescription = "Descripción de la imagen",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
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
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.weight(1f) // Hace que el texto ocupe el espacio disponible
                        )
                        Button(
                            onClick = { /* Lógica del botón */ },
                            modifier = Modifier.padding(start = 16.dp).padding(end=10.dp) // Añade padding al botón
                        ) {
                            Text(text = "Editar Perfil")
                        }
                    }
                    Text(
                        text = usuario.email,
                        style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = modifier.height(16.dp))
                    if(usuario.biografia.isEmpty()){
                        Text(
                            text = "Aún no sabemos nada sobre ti...",
                            modifier = Modifier.padding(bottom = 10.dp).padding(end = 40.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
                        )
                    }else {
                        Text(
                            text = usuario.biografia,
                            modifier = Modifier.padding(bottom = 10.dp).padding(end = 40.dp),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if(usuario.generofav.isEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_music_note_24),
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(16.dp) // Tamaño del icono
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
                                tint = Color.LightGray,
                                modifier = Modifier.size(16.dp) 
                            )
                            Text(
                                text = usuario.generofav,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                    Divider(
                        color = Color.White,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp).padding(end= 32.dp)
                    )
                }
            }
        }
    }