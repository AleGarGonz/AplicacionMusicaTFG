package com.example.aplicacionmusicatfg.componentes.ComponentesComunes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.MusicPlayerController
import com.example.aplicacionmusicatfg.modelos.Cancion
import com.example.aplicacionmusicatfg.navigation.Rutas
import java.io.File

//Fichero con todos los componentes que se utilizan en varias pantallas, para tratar de no repetir codigo

//Item de cancion para los LazyColumns
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancionItem(cancion: Cancion, listCanciones:List<File>, musicPlayerController: MusicPlayerController, navController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val onAnteriorClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playPrevious() }}
    val onStopClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.stopAndReset() }}
    val onPlayClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playOrPause()} }
    val onSiguienteClick: () -> Unit = { if(listCanciones.size >=1){musicPlayerController.playNext()} }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start=19.dp).padding(end = 26.dp).padding(vertical = 4.dp)
            .clickable {
                isSheetOpen = true
                musicPlayerController.setListAndIndex(listCanciones, obtenerPosicionArchivo(listCanciones, cancion.audio))},
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.Cyan)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.background(Color.White)) {
            var isClicked = false
            Column(
                Modifier
                .padding(start = 8.dp).padding(top = 3.dp)
                .height(60.dp)
                .width(300.dp)
            ) {
                Text(
                    text = "${cancion.titulo}",
                    fontSize = 21.sp,
                    color = Color.Black
                )
                Text(
                    text = "${cancion.artista}",
                    fontSize = 17.sp,
                    color = Color.Black
                )
            }
            IconButton(
                onClick = {
                    isClicked =true
                    navController.navigate(
                        route = Rutas.CancionScreen.createRoute(
                            cancion.artista,
                            cancion.audio,
                            cancion.genero,
                            cancion.imagen,
                            cancion.titulo
                        )
                    )
                },
                modifier = Modifier.weight(1f),
                enabled = !isClicked
            ) {
                val stopIcon = painterResource(id = R.drawable.baseline_open_in_new_24)
                Icon(painter = stopIcon, contentDescription = "MasInfo",tint = Color.Black)
            }

        }
        if(isSheetOpen){
            ModalBottomSheet(
                containerColor= Color.Cyan,
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                    musicPlayerController.release()

                }) {
                if(listCanciones.size >=1){
                    Reproductor(
                        onAnteriorClick = onAnteriorClick,
                        onStopClick = onStopClick,
                        onPlayClick = onPlayClick,
                        onSiguienteClick = onSiguienteClick
                    )
                }else{
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = "No se puede reproducir la canción en estos momentos",
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                }
            }
        }
    }
}
//Metodo para obtener la posicion del mp3 en la lista
 fun obtenerPosicionArchivo(files: List<File>, nombreArchivo: String): Int {
    for ((index, file) in files.withIndex()) {
        if (file.absoluteFile.name.contains(nombreArchivo)) {
            return index
        }
    }
    return -1
}
//Botones para manejar las canciones en las listas
@Composable
fun Reproductor(
    onAnteriorClick: () -> Unit,
    onStopClick: () -> Unit,
    onPlayClick: () -> Unit,
    onSiguienteClick: () -> Unit
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (!isPlaying) {
                    isPlaying = true
                }
                onAnteriorClick()
            }, modifier = Modifier.weight(1f)) {
                val anteriorIcon = painterResource(id = R.drawable.baseline_skip_previous_24)
                Icon(painter = anteriorIcon, contentDescription = "Anterior",tint = Color.Black)
            }
            IconButton(onClick = {
                if (isPlaying) {
                    isPlaying = false
                    onPlayClick()
                }
                onStopClick()
            }, modifier = Modifier.weight(1f)) {
                val stopIcon = painterResource(id = R.drawable.baseline_stop_24)
                Icon(painter = stopIcon, contentDescription = "Stop",tint = Color.Black)
            }
            IconButton(
                onClick = {
                    isPlaying = !isPlaying
                    onPlayClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                val playIcon = if (isPlaying) {
                    painterResource(id = R.drawable.baseline_pause_24)
                } else {
                    painterResource(id = R.drawable.baseline_play_arrow_24)
                }
                Icon(painter = playIcon, contentDescription = if (isPlaying) "Pause" else "Play",tint = Color.Black)
            }
            IconButton(onClick = {
                if (!isPlaying) {
                    isPlaying = true
                }
                onSiguienteClick()
            }, modifier = Modifier.weight(1f)) {
                val siguienteIcon = painterResource(id = R.drawable.baseline_skip_next_24)
                Icon(painter = siguienteIcon, contentDescription = "Siguiente",tint = Color.Black)
            }
        }
    }
}
//Valida la lista de audios para que no haya errores
fun validarAudioLista(listaArchivos: List<File?>): Boolean {
    return listaArchivos.all {
        it != null &&
                it.isFile &&
                it.exists() &&
                it.length() > 0L
    }
}

//Textos que se compartan en las pantallas del loguin o algunos incluso en el perfil
@Composable
fun generoFavText(modifier: Modifier) {
    Text(
        text = "Genero Favortito",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
        modifier = modifier
    )
}

@Composable
fun biografiaText(modifier: Modifier) {
    Text(
        text = "Biografia",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
        modifier = modifier
    )
}

@Composable
fun emailText(modifier: Modifier) {
    Text(
        text = "Email",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
        modifier = modifier
    )
}

@Composable
fun nombreText(modifier: Modifier) {
    Text(
        text = "Nombre",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
        modifier = modifier
    )
}
@Composable
fun ParaContinuarText(modifier: Modifier) {
    Text(
        text = "para continuar",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
        modifier = modifier
    )
}

@Composable
fun PasswordText(modifier: Modifier) {
    Text(
        text = "Password",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFB5B5B5),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {
    var showPassword by rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        trailingIcon = {
            val imagen = if (showPassword) {
                R.drawable.baseline_visibility_off_24
            } else {
                R.drawable.baseline_visibility_24
            }


            Icon(
                painter = painterResource(id = imagen),
                contentDescription = "View",
                modifier = Modifier.clickable { showPassword = !showPassword }
            )
        },
        placeholder = { Text(text = "Password") },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Gray,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Gray,
            containerColor = Color(0xFFFAFAFA),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (showPassword) {
            VisualTransformation.None

        } else {
            PasswordVisualTransformation()
        }
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