package com.example.aplicacionmusicatfg.navigation

sealed class Rutas(val route:String){
    object BusquedaScreen:Rutas("busqueda_screen")
    object CancionScreen : Rutas("cancion_screen/{parametro}/{parametro2}/{parametro3}/{parametro4}/{parametro5}") {
        fun createRoute(parametro: String, parametro2: String, parametro3: String, parametro4: String, parametro5: String): String {
            return "cancion_screen/$parametro/$parametro2/$parametro3/$parametro4/$parametro5"
        }
    }
}