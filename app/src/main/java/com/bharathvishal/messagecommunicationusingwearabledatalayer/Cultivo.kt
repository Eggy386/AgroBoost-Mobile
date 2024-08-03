package com.bharathvishal.messagecommunicationusingwearabledatalayer

data class Cultivo(
    val id: String,
    val tipoCultivo: String,
    val tipoRiego: String,
    val programaRiego: String,
    val metodoFertilizacion: String,
    val fechaFertilizacion: String,
    val cantidadFertilizante: Int,
    val controlPlagas: String,
    val tecnicaPolinizacion: String,
    val medidasSiembra: Int,
    val fechaPrevista: String,
    val idUsuario: String
)
