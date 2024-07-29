package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

class Forgot: ComponentActivity() {

    private lateinit var correo: String

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
            val correoInput = this.findViewById<EditText>(R.id.editTextCorreoForgot).text.toString()
            correo = correoInput.toString()

            if(correo.isEmpty()) {
                showDialogWarning()
            } else {
                send_mail(correo)
            }
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
        textTitle.text = "Correo enviado"

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoSuccess)
        textInfo.text = "Se ha enviado un correo con el código de verificación."

        val buttonSuccess: Button = dialog.findViewById(R.id.buttonCloseSuccess)
        buttonSuccess.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@Forgot, Verificacion::class.java)
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

    private fun send_mail(correo: String) {
        val url = "http://192.168.1.23:4000/send-mail"

        val jsonParams = JSONObject().apply {
            put("correo_electronico", correo)
        }

        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            Response.Listener { response ->
                try {
                    val mensaje = response.getString("mensaje")
                    if (mensaje == "Correo enviado") {
                        showDialogSuccess()
                    } else {
                        showDialogError("Error", "Ocurrió un error inesperado.")
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
}