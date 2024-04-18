package com.example.aplicacionmusicatfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aplicacionmusicatfg.navigation.NaveegacionScreens
import com.example.aplicacionmusicatfg.ui.theme.AplicacionMusicaTFGTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.database


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AplicacionMusicaTFGTheme {
                // Inicializa Firebase
                FirebaseApp.initializeApp(this)
                Firebase.database.setPersistenceEnabled(true)//Tengo que manejar esto para que no inicie la app con el movil sin conexion
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NaveegacionScreens()
                }
            }
        }
    }
}
