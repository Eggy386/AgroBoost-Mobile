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
import android.view.Gravity
import android.view.ViewGroup

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
            showDialogSuccess()
            //showDialogError()
            //showDialogWarning()
            //showDialogDelete()
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

    private fun showDialogError() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.error_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleError)
        textTitle.text = ("Credenciales incorrectas")

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoError)
        textInfo.text = ("No fue posible iniciar sesión")

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

        val buttonWarning: Button = dialog.findViewById(R.id.buttonCancelWarning)
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
}