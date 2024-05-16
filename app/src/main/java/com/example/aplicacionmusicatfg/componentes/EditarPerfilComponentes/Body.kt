package com.example.aplicacionmusicatfg.componentes.EditarPerfilComponentes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aplicacionmusicatfg.R
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.controladores.UsuarioController
import com.example.aplicacionmusicatfg.controladores.getImagenStorage
import com.example.aplicacionmusicatfg.controladores.subirImagenStorage
import com.example.aplicacionmusicatfg.modelos.Usuario
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream


@Composable
fun Body(
    modifier: Modifier,
    logincontrol: LoginController,
    navController: NavController,
    usuarioController: UsuarioController
) {
    var usuario: Usuario? by remember { mutableStateOf(null) }

    val emailBuscado = logincontrol.getCurrentUser()?.email.toString()

    var ImagenFile by remember { mutableStateOf<File?>(null) }
    var ImagenFileActualizado by remember { mutableStateOf<File?>(null) }
    var imagenActualizada by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var nombre by rememberSaveable {
        mutableStateOf("")
    }
    var biografia by rememberSaveable {
        mutableStateOf("")
    }
    var genero by rememberSaveable {
        mutableStateOf("")
    }

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
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .height(140.dp)
                .background(
                    color = Color.Black//Poner un fondo wapo o algo
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
                .padding(top = 85.dp)
                .padding(start = 4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = modifier
                    .padding(top = 10.dp)
                    .padding(start = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Función composable para mostrar la imagen
                if (imagenActualizada) {
                    println("El archivo que acabo de seleccionar:"+ImagenFile?.absolutePath)
                    ShowImageActualizado(ImagenFileActualizado)
                } else {
                    ShowImage(ImagenFile)
                }

                // Botón para seleccionar una imagen
                ImageSelectionButton(onImageSelected = { file ->
                    ImagenFileActualizado = file
                    println("El archivo que acabo de seleccionar:"+file.absolutePath)
                    imagenActualizada = true
                }, context = context)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = 14.dp)
            ) {
                Spacer(modifier = Modifier.size(10.dp))
                Nombre(nombre,usuario.nombre) {
                    nombre = it
                }
                Spacer(modifier = modifier.height(16.dp))
                if(usuario.biografia.isEmpty()){
                   Biografia(bio = biografia, bioActual ="Aún no sabemos nada sobre ti..." ){
                       biografia = it
                   }
                }else {
                    Biografia(bio = biografia, bioActual =usuario.biografia ){
                        biografia = it
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
                if(usuario.generofav.isEmpty()) {
                    Genero(gen = genero, genActual = "¿Cual es tu genero favorito?"){
                        genero = it
                    }
                }else{
                    Genero(gen = genero, genActual = usuario.generofav){
                        genero = it
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
                BotonActualizar() {
                    if (imagenActualizada) {
                        ImagenFileActualizado?.let { subirImagenStorage(file = it) }
                        // Obtener el nombre del archivo sin la extensión
                        val fileNameWithoutExtension = ImagenFileActualizado?.name.toString()
                        usuario.fotoperfil = fileNameWithoutExtension
                    }
                    if(biografia != usuario.biografia && biografia.isNotEmpty()){
                        usuario.biografia = biografia
                    }
                    if(genero != usuario.generofav && genero.isNotEmpty()){
                        usuario.generofav = genero
                    }
                    if(nombre != usuario.nombre && nombre.isNotEmpty()){
                        usuario.nombre = nombre
                    }
                    usuarioController.actualizarUsuario(usuario) { exito ->
                        if (exito) {
                            Toast.makeText(context, "Actualizacion exitosa!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No se pudo actualizar!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageSelectionButton(onImageSelected: (File) -> Unit, context: Context) {
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = getFileFromUri(it, context)
            if (file != null) {
                onImageSelected(file)
            }
        }
    }

    IconButton(
        onClick = {
            scope.launch {
                launcher.launch("image/*")
            }
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_edit_24),
            contentDescription = "Cambiar imagen"
        )
    }
}

fun getFileFromUri(uri: Uri, context: Context): File? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = columnIndex?.let { cursor.getString(it) }
    cursor?.close()
    return filePath?.let { File(it) }
}

@Composable
fun ShowImage(ImagenFile: File?) {
    if (ImagenFile != null) {
        val bitmap: Bitmap = BitmapFactory.decodeFile(ImagenFile.absolutePath)
        val imageBitmap = bitmap.asImageBitmap()
        Image(
            bitmap = imageBitmap,
            contentDescription = "Foto perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.imagenperfilpordefecto),
            contentDescription = "Descripción de la imagen",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds
        )
    }
}
@Composable
fun ShowImageActualizado(ImagenFile: File?) {
    println("El archivo del album pero en show image: "+ImagenFile?.absolutePath)
    if (ImagenFile != null) {
        val inputStream = FileInputStream(ImagenFile)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        println("Biitmap : "+bitmap)
        val imageBitmap = bitmap?.asImageBitmap()
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Foto perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
            )
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.imagenperfilpordefecto),
            contentDescription = "Descripción de la imagen",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.FillBounds
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nombre(nombre: String,nombreActual:String, onTextChanged: (String) -> Unit) {
    TextField(
        value = nombre,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 30.dp),
        placeholder = { Text(text = nombreActual,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        ) },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.Gray,
            containerColor = Color.Gray.copy(alpha = 0.5f),
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedLabelColor = Color.White


        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Biografia(bio: String,bioActual:String, onTextChanged: (String) -> Unit) {
    TextField(
        value = bio,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 30.dp),
        placeholder = { Text(text = bioActual,
            style = TextStyle(
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
        ) },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.Gray,
            containerColor = Color.Gray.copy(alpha = 0.5f),
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedLabelColor = Color.White


        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Genero(gen: String,genActual:String, onTextChanged: (String) -> Unit) {
    TextField(
        value = gen,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 30.dp),
        placeholder = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_music_note_24),
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = genActual,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )
            }},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.Gray,
            containerColor = Color.Gray.copy(alpha = 0.5f),
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            focusedLabelColor = Color.White


        )
    )
}
@Composable
fun BotonActualizar(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp).padding(end = 30.dp)
    ) {
        Text(text = "Actualizar")
    }
}
