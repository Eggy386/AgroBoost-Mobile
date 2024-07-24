package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.ComponentActivity


class Verificacion: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verificacion)

        Log.d("Verificacion", "Actividad Verificacion cargada")


        // Get references to your EditText views
        val editTextCodigo1 = findViewById<EditText>(R.id.editTextCodigo1)
        val editTextCodigo2 = findViewById<EditText>(R.id.editTextCodigo2)
        val editTextCodigo3 = findViewById<EditText>(R.id.editTextCodigo3)
        val editTextCodigo4 = findViewById<EditText>(R.id.editTextCodigo4)

        class EditTextListener(
            private val currentEditText: EditText,
            private val nextEditText: EditText?
        ) : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Limit text to one digit
                if (s?.length ?: 0 > 1) {
                    currentEditText.setText(s?.subSequence(s.length - 1, s.length))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Change focus to next EditText if it exists
                if (nextEditText != null && s?.isNotEmpty() == true) {
                    nextEditText.requestFocus()
                }
            }
        }

        // Create EditTextListener instances
        val listener12: EditTextListener = EditTextListener(editTextCodigo1, editTextCodigo2)
        val listener23: EditTextListener = EditTextListener(editTextCodigo2, editTextCodigo3)
        val listener34: EditTextListener = EditTextListener(editTextCodigo3, editTextCodigo4)


        // Set the TextWatcher on each EditText
        editTextCodigo1.addTextChangedListener(listener12)
        editTextCodigo2.addTextChangedListener(listener23)
        editTextCodigo3.addTextChangedListener(listener34)

        val rowBack: ImageView = findViewById(R.id.imageViewRowBackVerificacion)
        rowBack.setOnClickListener {
            val intent = Intent(this@Verificacion, Forgot::class.java)
            finish()
        }

        val viewNewPass: Button = findViewById(R.id.buttonVerificacion)
        viewNewPass.setOnClickListener {
            val intent = Intent(this@Verificacion, NewPassword::class.java)
            startActivity(intent)
        }
    }
}