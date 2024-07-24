package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.bharathvishal.messagecommunicationusingwearabledatalayer.Login

class NewPassword: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_password)

        val rowBack: ImageView = findViewById(R.id.imageViewRowBackNewPass)
        rowBack.setOnClickListener {
            val intent = Intent(this@NewPassword, Verificacion::class.java)
            finish()
        }

        val login: Button = findViewById(R.id.buttonNewPass)
        login.setOnClickListener {
            val intent = Intent(this@NewPassword, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}