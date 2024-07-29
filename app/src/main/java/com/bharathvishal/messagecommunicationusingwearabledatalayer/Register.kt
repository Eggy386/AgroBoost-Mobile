package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Register: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        val register: Button = findViewById(R.id.buttonRegister)
        register.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.editTextNombres).text.toString()
            val apePaterno = findViewById<EditText>(R.id.editTextApePaterno).text.toString()
            val apeMaterno = findViewById<EditText>(R.id.editTextApeMaterno).text.toString()
            val correo = findViewById<EditText>(R.id.editTextCorreo).text.toString()
            val contrasena = findViewById<EditText>(R.id.editTextPassword).text.toString()

            if(nombre.isEmpty() || apePaterno.isEmpty() || apeMaterno.isEmpty() || correo.isEmpty() || contrasena.isEmpty()){
                showDialogWarning()
            } else {
                register(nombre, apePaterno, apeMaterno, correo, contrasena)
            }
        }

        val viewLogin: TextView = findViewById(R.id.textViewInicioSesion)
        viewLogin.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }
    }

    private fun showDialogSuccess() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sucess_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleSucess)
        textTitle.text = ("¡Registro exitoso!")

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoSuccess)
        textInfo.text = ("Felicidades, has completado el registro exitosamente.")

        val buttonSuccess: Button = dialog.findViewById(R.id.buttonCloseSuccess)
        buttonSuccess.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
            finish()
        }

        dialog.show()

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
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


    private fun showDialogWarning() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.warning_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleWarning)
        textTitle.text = ("Advertencia")

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoWarning)
        textInfo.text = ("Completa la información requerida")

        val buttonWarning: Button = dialog.findViewById(R.id.buttonConfirmWarning)
        buttonWarning.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
    }

    private fun register(nombre: String, apePaterno: String, apeMaterno: String, correo: String, contrasena: String){
        val url = "http://192.168.1.23:4000/register"

        val jsonParams = JSONObject().apply {
            put("nombre", nombre)
            put("apellido_paterno", apePaterno)
            put("apellido_materno", apeMaterno)
            put("correo_electronico", correo)
            put("contrasena", contrasena)
            put("rol", 0)
        }

        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            Response.Listener { response ->
                if (response.getBoolean("success")) {
                    showDialogSuccess()
                } else {
                    showDialogError(
                        "Correo electronico en uso",
                        "El correo electrónico que has proporcionado ya está en uso. Por favor, intenta con otro correo electrónico."
                    )
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val statusCode = error.networkResponse?.statusCode
                when (statusCode) {
                    401 -> {
                        showDialogError(
                            "Correo electronico en uso",
                            "El correo electrónico que has proporcionado ya está en uso. Por favor, intenta con otro correo electrónico."
                        )
                    }
                    500 -> {
                        showDialogError(
                            "Error del sistema",
                            "Ocurrió un error al intentar conectar con el servidor. Por favor, inténtalo más tarde."
                        )
                    }
                    else -> {
                        Log.e("LoginError", "Error desconocido: ${error.message}")
                        showDialogError(
                            "Error desconocido",
                            "Ocurrió un error inesperado. Por favor, inténtalo más tarde."
                        )
                    }
                }
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
}