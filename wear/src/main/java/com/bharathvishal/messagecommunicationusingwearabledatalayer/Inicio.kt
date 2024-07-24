package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class Inicio: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        val linearCultivo: LinearLayout = findViewById(R.id.linearCultivo)
        linearCultivo.setOnClickListener {
            val intent = Intent(this, Cultivo::class.java)
            startActivity(intent)
        }

        val linearRecordatorio: LinearLayout = findViewById(R.id.linearRecordatorio)
        linearRecordatorio.setOnClickListener {
            val intent = Intent(this, Recordatorio::class.java)
            startActivity(intent)
        }

        val linearRiego: LinearLayout = findViewById(R.id.linearRiego)
        linearRiego.setOnClickListener {
            val intent = Intent(this, Riego::class.java)
            startActivity(intent)
        }
    }
}