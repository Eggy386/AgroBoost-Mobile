package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Login: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val viewRegister: TextView = findViewById(R.id.textViewRegistro)
        viewRegister.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        val viewForgot: TextView = findViewById(R.id.textViewForgot)
        viewForgot.setOnClickListener {
            val intent = Intent(this@Login, Forgot::class.java)
            startActivity(intent)
        }

        val viewAgroboost: Button = findViewById(R.id.buttonLogin)
        viewAgroboost.setOnClickListener {
            val correo = findViewById<EditText>(R.id.editTextCorreo).text.toString()
            val contrasena = findViewById<EditText>(R.id.editTextPassword).text.toString()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                showDialogWarning()
            } else {
                login(correo, contrasena)
            }
        }
    }

    private fun showDialogSuccess() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sucess_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleSucess)
        textTitle.text = ("Bienvenido")

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoSuccess)
        textInfo.text = ("Inicio de sesión éxitoso.")

        val buttonSuccess: Button = dialog.findViewById(R.id.buttonCloseSuccess)
        buttonSuccess.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@Login, Inicio::class.java)
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

    private fun showDialogDelete() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleDelete)
        textTitle.text = ("¿Seguro que desea eliminarlo?")

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoDelete)
        textInfo.text = ("Esta acción no se puede restablecer.")

        val buttonDelete: Button = dialog.findViewById(R.id.buttonCloseDelete)
        buttonDelete.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
    }

    private fun login(correo: String, contrasena: String) {
        val url = "http://192.168.50.23:4000/login" // Reemplaza con la IP de tu servidor

        // Crea el objeto JSON para enviar
        val jsonParams = JSONObject().apply {
            put("correo_electronico", correo)
            put("contrasena", contrasena)
        }

        val request = object : JsonObjectRequest(
            Method.POST, // Método HTTP
            url, // URL del servidor
            jsonParams, // Parámetros JSON
            Response.Listener { response ->
                Log.d("LoginResponse", response.toString())
                if (response.getBoolean("success") && response.getBoolean("success")) {
                    showDialogSuccess()
                } else {
                    Log.d("LoginActivity", "Credenciales incorrectas")
                    showDialogError(
                        "Credenciales incorrectas",
                        "No fue posible iniciar sesión"
                    )
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val statusCode = error.networkResponse?.statusCode
                when (statusCode) {
                    401 -> {
                        Log.d("LoginActivity", "Credenciales incorrectas")
                        showDialogError(
                            "Credenciales incorrectas",
                            "No fue posible iniciar sesión"
                        )
                    }
                    500 -> {
                        Log.e("LoginError", "Error del servidor")
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
            // Redefine el método para obtener cabeceras
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