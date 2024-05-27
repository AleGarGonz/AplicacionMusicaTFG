package com.example.aplicacionmusicatfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.aplicacionmusicatfg.componentes.ComponentesComunes.ImageLogo
import com.example.aplicacionmusicatfg.navigation.NaveegacionScreens
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NaveegacionScreens()
                    MiSplashScreen()
                }
            }
        }
    }
}

class FirebasePersistenceViewModel : ViewModel() {
    var isPersistenceEnabled: Boolean = false
}
@Composable
fun MiSplashScreen() {
    var isVisible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        isVisible = false
    }
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ImageLogo(Modifier.size(200.dp))
                Spacer(modifier = Modifier.height(16.dp))
                BienvenidoText(Modifier)
            }
        }
    }
}
@Composable
private fun BienvenidoText(modifier: Modifier) {
    Text(
        text = "Bienvenido",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF000000),
        modifier = modifier
    )
}