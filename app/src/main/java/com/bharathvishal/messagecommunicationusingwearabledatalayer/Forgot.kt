package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class Forgot: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot)

        val rowBack: ImageView = findViewById(R.id.imageViewRowBackForgot)
        rowBack.setOnClickListener {
            val intent = Intent(this@Forgot, Login::class.java)
            finish()
        }

        val sendMail: Button = findViewById(R.id.buttonSendMail)
        sendMail.setOnClickListener {
            val intent = Intent(this@Forgot, Verificacion::class.java)
            startActivity(intent)
        }
    }
}