package com.example.aplicacionmusicatfg.navigation

sealed class Rutas(val route:String){
    object BusquedaScreen:Rutas("busqueda_screen?parametroOpcional={parametroOpcional}"){
        fun createRoute(parametroOpcional: String) = "busqueda_screen?parametroOpcional=$parametroOpcional"
    }
    object CancionScreen : Rutas("cancion_screen/{parametro}/{parametro2}/{parametro3}/{parametro4}/{parametro5}") {
        fun createRoute(parametro: String, parametro2: String, parametro3: String, parametro4: String, parametro5: String): String {
            return "cancion_screen/$parametro/$parametro2/$parametro3/$parametro4/$parametro5"
        }
    }
    object GenerosScreen:Rutas("generos_screen")
    object Login : Rutas("login_screen")
    object Registro : Rutas("registro_screen")
    object RecuperarContra : Rutas("recuperarcontra_screen")
    object PerfilUsuario : Rutas("perfilusuario_screen")

    object EditarPerfil : Rutas("editarperfil_screen")
    object ListaRepro : Rutas("listarepro_screen")
}