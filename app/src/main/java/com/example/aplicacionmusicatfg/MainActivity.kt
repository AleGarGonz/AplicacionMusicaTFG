package com.example.aplicacionmusicatfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.ui.theme.AplicacionMusicaTFGTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    private val firebasePersistenceViewModel: FirebasePersistenceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase y habilita la persistencia de datos si aún no se ha hecho
        if (!firebasePersistenceViewModel.isPersistenceEnabled) {
            FirebaseApp.initializeApp(this)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            firebasePersistenceViewModel.isPersistenceEnabled = true
        }

        setContent {
            AplicacionMusicaTFGTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //NaveegacionScreens();
                    LoginScreen()
                }
            }
        }
    }
}

class FirebasePersistenceViewModel : ViewModel() {
    var isPersistenceEnabled: Boolean = false
}
