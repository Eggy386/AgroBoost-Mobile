package com.bharathvishal.messagecommunicationusingwearabledatalayer

object UserSingleton {
    var id: String? = null
    var cultivos: List<Cultivo> = mutableListOf()
    var recordatorios: List<Recordatorio> = mutableListOf()
    var riegos: List<Riego> = mutableListOf()
}