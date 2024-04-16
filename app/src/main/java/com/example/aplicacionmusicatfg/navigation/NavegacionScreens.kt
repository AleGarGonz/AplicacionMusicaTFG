package com.example.aplicacionmusicatfg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aplicacionmusicatfg.view.BusquedaScreen
import com.example.aplicacionmusicatfg.view.CancionScreen

//No puedo pasar objetos enteros porque navigation de jetpack compose no deja pasar objetos
//Estuve haciendo pruebas transformando el objeto a json y luego destransformandolo pero me dejaba daba muchos problemas
@Composable
fun NaveegacionScreens(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Rutas.BusquedaScreen.route){
        composable(route= Rutas.BusquedaScreen.route){
            BusquedaScreen(navController)
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
    }
}
