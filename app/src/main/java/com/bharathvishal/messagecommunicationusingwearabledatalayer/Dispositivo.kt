package com.bharathvishal.messagecommunicationusingwearabledatalayer

data class Dispositivo(
    val id: String,
    val nombreDispositivo: String,
    val datos: Any, // Puede ser JSONArray o JSONObject dependiendo del tipo de sensor
    val idCultivo: Cultivo,
    val idUsuario: String
)
