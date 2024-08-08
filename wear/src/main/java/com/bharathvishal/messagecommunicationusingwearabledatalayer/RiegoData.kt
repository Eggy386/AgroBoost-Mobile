package com.bharathvishal.messagecommunicationusingwearabledatalayer

data class RiegoData(
    val id: String,
    val id_usuario: String,
    val hora_riego: String,
    val dias_riego: String,
    var activo: Boolean
)
