package com.example.aplicacionmusicatfg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aplicacionmusicatfg.LoginScreen
import com.example.aplicacionmusicatfg.controladores.LoginController
import com.example.aplicacionmusicatfg.view.BusquedaScreen
import com.example.aplicacionmusicatfg.view.CancionScreen
import com.example.aplicacionmusicatfg.view.GenerosScreen
import com.example.aplicacionmusicatfg.view.RegistroScreen
import com.google.firebase.auth.FirebaseUser

//No puedo pasar objetos enteros porque navigation de jetpack compose no deja pasar objetos
//Estuve haciendo pruebas transformando el objeto a json y luego destransformandolo pero me dejaba daba muchos problemas

@Composable
fun NaveegacionScreens(){
    val navController = rememberNavController()
    val logincontrol = LoginController()
    val user :FirebaseUser? = logincontrol.getCurrentUser()

    NavHost(navController = navController, startDestination = if(user == null)Rutas.Login.route else Rutas.GenerosScreen.route){
        composable(route = Rutas.BusquedaScreen.route,
            arguments = listOf(navArgument("parametroOpcional") {
                defaultValue = "Default"
            })){backStackEntry ->
            BusquedaScreen(
                navController = navController,
                genero  = backStackEntry.arguments?.getString("parametroOpcional")
            )
        }
        composable(
            Rutas.CancionScreen.route,
            arguments = listOf(
                navArgument("parametro") { type = NavType.StringType },
                navArgument("parametro2") { type = NavType.StringType },
                navArgument("parametro3") { type = NavType.StringType },
                navArgument("parametro4") { type = NavType.StringType },
                navArgument("parametro5") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CancionScreen(
                navController = navController,
                param1 = backStackEntry.arguments?.getString("parametro") ?: "",
                param2 = backStackEntry.arguments?.getString("parametro2") ?: "",
                param3 = backStackEntry.arguments?.getString("parametro3") ?: "",
                param4 = backStackEntry.arguments?.getString("parametro4") ?: "",
                param5 = backStackEntry.arguments?.getString("parametro5") ?: ""
            )
        }
        composable(route= Rutas.GenerosScreen.route){
            GenerosScreen(navController,logincontrol)
        }
        composable(route= Rutas.Login.route){
            LoginScreen(navController,logincontrol)
        }
        composable(route= Rutas.Registro.route){
            RegistroScreen(navController,logincontrol)
        }
    }
}
