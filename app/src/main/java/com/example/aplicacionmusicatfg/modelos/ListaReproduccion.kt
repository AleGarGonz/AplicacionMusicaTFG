package com.example.aplicacionmusicatfg.modelos

import java.util.UUID

class ListaReproduccion {
    val id: String = UUID.randomUUID().toString()
    var Listanombre: String =""
    var Canciones: List<String> = emptyList()
}