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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class NewPassword : ComponentActivity() {
    private var correo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_password)

        // Obtener el correo del Intent
        correo = intent.getStringExtra("correo_electronico")

        val rowBack: ImageView = findViewById(R.id.imageViewRowBackNewPass)
        rowBack.setOnClickListener {
            finish()
        }

        val buttonNewPass: Button = findViewById(R.id.buttonNewPass)
        buttonNewPass.setOnClickListener {
            val newPassword = findViewById<EditText>(R.id.editTextNewPass).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.editTextCorreoForgot).text.toString()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showDialogWarning("Advertencia", "Completa la información requerida")
            } else if (newPassword != confirmPassword) {
                showDialogWarning("Advertencia", "Las contraseñas no coinciden")
            } else if (newPassword.length < 8) {
                showDialogWarning("Advertencia","La contraseña debe tener al menos 8 caracteres")
            } else {
                updatePassword(correo!!, newPassword)
            }
        }
    }

    private fun updatePassword(correo: String, newPassword: String) {
        val url = "http://192.168.50.23:4000/updatePass"

        val jsonParams = JSONObject().apply {
            put("correo_electronico", correo)
            put("contrasena", newPassword)
        }

        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            Response.Listener { response ->
                try {
                    showDialogSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
                    showDialogError("Errro", "Ocurrió un error al procesar la respuesta")
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                showDialogError("Error","Error al actualizar la contraseña. Por favor, inténtelo más tarde.")
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
        textTitle.text = "Accion exitosa"

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoSuccess)
        textInfo.text = "Su contraseña ha sido actualizada."

        val buttonSuccess: Button = dialog.findViewById(R.id.buttonCloseSuccess)
        buttonSuccess.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@NewPassword, Login::class.java)
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

    private fun showDialogWarning(title: String, message: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.warning_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleWarning)
        textTitle.text = title

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoWarning)
        textInfo.text = message

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
}
