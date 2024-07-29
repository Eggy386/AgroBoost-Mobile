package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Verificacion : ComponentActivity() {

    private var correo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verificacion)

        correo = intent.getStringExtra("correo_electronico")

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
            // Obtener el código de los EditText
            val codigo = "${editTextCodigo1.text}${editTextCodigo2.text}${editTextCodigo3.text}${editTextCodigo4.text}"

            // Llamar a la función para verificar el código
            if(correo != null) {
                verifyCode(correo!!, codigo)
            }
        }
    }

    private fun verifyCode(correo: String, codigo: String) {
        val url = "http://192.168.50.23:4000/verifyCode"

        val jsonParams = JSONObject().apply {
            put("correo_electronico", correo)
            put("code", codigo)
        }

        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            Response.Listener { response ->
                try {
                    val mensaje = response.getString("mensaje")
                    if (mensaje == "Código de verificación válido") {
                        showDialogSuccess()
                    } else {
                        showDialogError("Error", "Código de verificación inválido.")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showDialogError("Error", "Ocurrió un error al procesar la respuesta.")
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                showDialogError("Error", "Ocurrió un error en el servidor. Por favor, inténtalo más tarde.")
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    private fun showDialogError(title: String, message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.error_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleError)
        textTitle.text = title

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoError)
        textInfo.text = message

        val buttonError: Button = dialog.findViewById(R.id.buttonCloseError)
        buttonError.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
    }

    private fun showDialogSuccess() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sucess_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleSucess)
        textTitle.text = "Verificación Exitosa"

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoSuccess)
        textInfo.text = "Código de verificación correcto."

        val buttonSuccess: Button = dialog.findViewById(R.id.buttonCloseSuccess)
        buttonSuccess.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@Verificacion, NewPassword::class.java)
            intent.putExtra("correo_electronico", correo)
            startActivity(intent)
        }

        dialog.show()

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
    }
}
