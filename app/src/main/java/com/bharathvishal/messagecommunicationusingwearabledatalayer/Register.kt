package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class Register: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val viewLogin: TextView = findViewById(R.id.textViewInicioSesion)
        viewLogin.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            finish()
        }
    }
}