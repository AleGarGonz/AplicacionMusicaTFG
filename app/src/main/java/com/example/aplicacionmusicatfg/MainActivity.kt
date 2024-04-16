package com.example.aplicacionmusicatfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.aplicacionmusicatfg.ui.theme.AplicacionMusicaTFGTheme
import com.example.aplicacionmusicatfg.view.BusquedaScreen
import com.example.aplicacionmusicatfg.view.CancionScreen
import com.google.firebase.FirebaseApp
import java.io.File


class MainActivity : ComponentActivity() {
    private val downloadedFileState = mutableStateOf<File?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AplicacionMusicaTFGTheme {
                // Inicializa Firebase
                FirebaseApp.initializeApp(this)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //LoginScreen()
                    //GenerosScreen()
                    //CancionScreen()
                    BusquedaScreen()
                }
            }
        }
    }

    /*private fun playDownloadedFile() {
        downloadedFileState.value?.let { file ->
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(file.path)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                }
            }, 15000)
        }
    }*/
}
