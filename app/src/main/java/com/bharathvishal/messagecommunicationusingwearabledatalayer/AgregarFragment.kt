package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AgregarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AgregarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var editTextDateAgregar: EditText
    private lateinit var editTextDateCosecha: EditText
    private lateinit var editTextProgramaRiego: EditText
    private lateinit var editTextCantidadFert: EditText
    private lateinit var editTextMedidas: EditText

    private lateinit var layoutFrambuesa: LinearLayout
    private lateinit var layoutMaiz: LinearLayout
    private var selectedCultivo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_agregar, container, false)

        // Initialize the EditTexts
        editTextDateAgregar = view.findViewById(R.id.editTextDateAgregar)
        editTextDateCosecha = view.findViewById(R.id.editTextDateCosecha)
        editTextProgramaRiego = view.findViewById(R.id.editTextProgramaRiego)
        editTextCantidadFert = view.findViewById(R.id.editTextCantidadFert)
        editTextMedidas = view.findViewById(R.id.editTextMedidas)

        // Set listeners for the date EditTexts
        setDateListener(editTextDateAgregar)
        setDateListener(editTextDateCosecha)

        // Obtener referencias a los spinners
        val spinnerTipoRiego: Spinner = view.findViewById(R.id.spinnerTipoRiego)
        val spinnerMetodoFert: Spinner = view.findViewById(R.id.spinnerMetodoFert)
        val spinnerTiempoFert: Spinner = view.findViewById(R.id.spinnerTiempoFert)
        val spinnerControlPlaga: Spinner = view.findViewById(R.id.spinnerControlPlaga)
        val spinnerTecnicaPol: Spinner = view.findViewById(R.id.spinnerTecnicaPol)

        // Configurar adaptadores para cada spinner
        setupSpinner(spinnerTipoRiego, R.array.tipo_riego_options)
        setupSpinner(spinnerMetodoFert, R.array.metodo_fertilizacion_options)
        setupSpinner(spinnerTiempoFert, R.array.tiempo_fertilizacion_options)
        setupSpinner(spinnerControlPlaga, R.array.control_plaga_options)
        setupSpinner(spinnerTecnicaPol, R.array.tecnica_polinizacion_options)

        layoutFrambuesa = view.findViewById(R.id.layoutFrambuesa)
        layoutMaiz = view.findViewById(R.id.layoutMaiz)

        layoutFrambuesa.setOnClickListener {
            selectedCultivo = "Frambuesa"
            layoutFrambuesa.setBackgroundResource(R.drawable.selected_contenedor)
            layoutMaiz.setBackgroundResource(R.drawable.contenedor_light)
        }

        layoutMaiz.setOnClickListener {
            selectedCultivo = "Maíz"
            layoutMaiz.setBackgroundResource(R.drawable.selected_contenedor)
            layoutFrambuesa.setBackgroundResource(R.drawable.contenedor_light)
        }

        val buttonRegistrar: Button = view.findViewById(R.id.buttonAgregarCultivo)
        buttonRegistrar.setOnClickListener {
            if (selectedCultivo != null) {
                val fechaSiembra = editTextDateAgregar.text.toString()
                val tipoRiego = spinnerTipoRiego.selectedItem.toString()
                val programaRiego = editTextProgramaRiego.text.toString()
                val metodoFertilizacion = spinnerMetodoFert.selectedItem.toString()
                val tiempoFertilizacion = spinnerTiempoFert.selectedItem.toString()
                val cantidadFertilizante = editTextCantidadFert.text.toString()
                val controlPlagas = spinnerControlPlaga.selectedItem.toString()
                val tecnicaPolinizacion = spinnerTecnicaPol.selectedItem.toString()
                val medidasSiembra = editTextMedidas.text.toString()
                val fechaCosecha = editTextDateCosecha.text.toString()

                val fechaSiembraISO = convertToISODate(fechaSiembra)
                val fechaCosechaISO = convertToISODate(fechaCosecha)
                val cantidadFertilizanteInt = cantidadFertilizante.toIntOrNull()
                val medidasSiembraInt = medidasSiembra.toIntOrNull()

                if (fechaSiembra.isEmpty() || tipoRiego.isEmpty() || programaRiego.isEmpty() ||
                    metodoFertilizacion.isEmpty() || tiempoFertilizacion.isEmpty() ||
                    controlPlagas.isEmpty() || tecnicaPolinizacion.isEmpty() || fechaCosecha.isEmpty()
                ) {
                    showDialogWarning(
                        "Advertencia",
                        "Completa la información requerida"
                    )
                } else if (cantidadFertilizanteInt == null || medidasSiembraInt == null){
                    showDialogWarning(
                        "Advertencia",
                        "Ingrese valores numéricos válidos para cantidad de fertilizante y medidas de la siembra."
                    )
                } else {
                    registerCultivo(
                        selectedCultivo!!,
                        fechaSiembraISO,
                        tipoRiego,
                        programaRiego,
                        metodoFertilizacion,
                        tiempoFertilizacion,
                        cantidadFertilizanteInt,
                        controlPlagas,
                        tecnicaPolinizacion,
                        medidasSiembraInt,
                        fechaCosechaISO
                    )
                }
            } else {
                showDialogWarning(
                    "Advertencia",
                    "Selecciona un tipo de cultivo"
                )
            }
        }

        return view
    }

    private fun setDateListener(editText: EditText) {
        editText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                    editText.setText(selectedDate)
                },
                year, month, day
            )

            datePickerDialog.show()
        }
    }

    private fun setupSpinner(spinner: Spinner, arrayResource: Int) {
        ArrayAdapter.createFromResource(
            requireContext(),
            arrayResource,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun convertToISODate(date: String): String {
        Log.v("convertToISODate", "$date")
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate: Date = sdf.parse(date)
            val isoSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoSdf.timeZone = TimeZone.getTimeZone("UTC")
            isoSdf.format(parsedDate)
        } catch (e: Exception) {
            ""
        }
    }

    private fun registerCultivo(
        tipoCultivo: String,
        fechaSiembra: String,
        tipoRiego: String,
        programaRiego: String,
        metodoFertilizacion: String,
        tiempoFertilizacion: String,
        cantidadFertilizante: Int,
        controlPlagas: String,
        tecnicaPolinizacion: String,
        medidasSiembra: Int,
        fechaCosecha: String
    ) {
        val userId = UserSingleton.id
        val url = "http://192.168.1.23:4000/registerCultivo" // Reemplaza con tu URL

        val jsonParams = JSONObject().apply {
            put("tipo_cultivo", tipoCultivo)
            put("fecha_siembra", fechaSiembra)
            put("tipo_riego", tipoRiego)
            put("programa_riego", programaRiego)
            put("metodo_fertilizacion", metodoFertilizacion)
            put("fechas_fertilizacion", tiempoFertilizacion)
            put("cantidad_fertilizante", cantidadFertilizante)
            put("control_plagas", controlPlagas)
            put("tecnica_polinizacion", tecnicaPolinizacion)
            put("medidas_siembra", medidasSiembra)
            put("fecha_prevista", fechaCosecha)
            put("id_usuario", userId)
        }

        Log.v("AgregarFragment", "$jsonParams")

        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            jsonParams,
            Response.Listener { response ->
                if (response.getBoolean("success")) {
                    showDialogSuccess()
                } else {
                    showDialogError(
                        "Error al registrar",
                        "Ocurrió un error al registrar su cultivo."
                    )
                }
            },
            Response.ErrorListener { error ->
                showDialogError(
                    "Error al registrar",
                    "Ocurrió un error interno en el servidor."
                )
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }

    private fun showDialogSuccess() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sucess_dialog)

        val textTitle: TextView = dialog.findViewById(R.id.textViewTitleSucess)
        textTitle.text = ("¡Registro exitoso!")

        val textInfo: TextView = dialog.findViewById(R.id.textViewInfoSuccess)
        textInfo.text = ("Felicidades, su cultivo ha sido registrado exitosamente.")

        val buttonSuccess: Button = dialog.findViewById(R.id.buttonCloseSuccess)
        buttonSuccess.setOnClickListener {
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
        val dialog = Dialog(requireContext())
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


    private fun showDialogWarning(title: String, message: String) {
        val dialog = Dialog(requireContext())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AgregarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AgregarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}