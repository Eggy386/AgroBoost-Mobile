package com.bharathvishal.messagecommunicationusingwearabledatalayer

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
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
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
 * Use the [RiegoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RiegoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var numPickerMin: NumberPicker
    private lateinit var numPickerSeg: NumberPicker
    private lateinit var numPickerAm: NumberPicker

    private lateinit var recyclerView: RecyclerView
    private lateinit var riegoAdapter: RiegoAdapter
    private val riegos = mutableListOf<Riego>()

    private var selectedDays: Int = 0

    private lateinit var layoutNoRiego: LinearLayout
    private lateinit var layoutConRiego: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun showBottomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheet_layout)

        val cancelButton = dialog.findViewById<Button>(R.id.buttonCancelRiego)

        cancelButton.setOnClickListener { dialog.dismiss() }

        numPickerMin = dialog.findViewById(R.id.numPickerMin)
        numPickerSeg = dialog.findViewById(R.id.numPickerSeg)
        numPickerAm = dialog.findViewById(R.id.numPickerAm)

        numPickerMin.minValue = 0
        numPickerMin.maxValue = 12

        numPickerSeg.minValue = 0
        numPickerSeg.maxValue = 59

        val str = arrayOf<String>("AM", "PM")
        numPickerAm.minValue = 0
        numPickerAm.maxValue = (str.size - 1)
        numPickerAm.displayedValues = str

        dialog.show()

        dialog.window!!.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.DialogAnimation
            setGravity(Gravity.BOTTOM)
        }

        dialog.findViewById<Button>(R.id.buttonSaveRiego).setOnClickListener {
            guardarRiego(dialog)
        }

        setupDayCheckBoxes(dialog)
    }

    private fun setupDayCheckBoxes(dialog: Dialog) {
        val days = listOf(
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnL), PickDayOfWeek.MONDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnM), PickDayOfWeek.TUESDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnX), PickDayOfWeek.WEDNESDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnJ), PickDayOfWeek.THURSDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnV), PickDayOfWeek.FRIDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnS), PickDayOfWeek.SATURDAY),
            Pair(dialog.findViewById<MaterialCheckBox>(R.id.btnD), PickDayOfWeek.SUNDAY)
        )

        days.forEach { (checkBox, day) ->
            checkBox.isChecked = hasDayOfWeek(selectedDays, day)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDays += day.value
                } else {
                    selectedDays -= day.value
                }
            }
        }
    }

    private fun hasDayOfWeek(value: Int, day: PickDayOfWeek): Boolean {
        return value and day.value != 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_riego, container, false)

        val fab = view.findViewById<FloatingActionButton>(R.id.addRiego)
        fab.setOnClickListener {
            showBottomDialog()
        }

        recyclerView = view.findViewById(R.id.recyclerViewRiegos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        layoutNoRiego = view.findViewById(R.id.layoutNoRiego)
        layoutConRiego = view.findViewById(R.id.layoutConRiego)

        riegoAdapter = RiegoAdapter(riegos) { riego ->
            toggleRiego(riego)
        }
        recyclerView.adapter = riegoAdapter

        fetchRiegos()

        return view
    }

    private fun fetchRiegos() {
        val url = "http://192.168.1.23:4000/riegos/${UserSingleton.id}"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                riegos.clear()
                if (response.length() == 0) {
                    layoutNoRiego.visibility = View.VISIBLE
                    layoutConRiego.visibility = View.GONE
                } else {
                    layoutNoRiego.visibility = View.GONE
                    layoutConRiego.visibility = View.VISIBLE
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)

                        // Formatear la hora de riego
                        val horaRiego = jsonObject.getString("hora_riego")
                        val formattedTime = formatDateToTime(horaRiego)

                        // Obtener y formatear los días de riego
                        val diasRiego = jsonObject.getJSONArray("dias_riego").let { jsonArray ->
                            List(jsonArray.length()) { jsonArray.getString(it) }
                        }
                        val formattedDiasRiego = formatDiasRiego(diasRiego)

                        val riego = Riego(
                            id = jsonObject.getString("_id"),
                            id_usuario = jsonObject.getString("id_usuario"),
                            hora_riego = formattedTime,
                            dias_riego = formattedDiasRiego,
                            activo = jsonObject.getBoolean("activo")
                        )
                        riegos.add(riego)
                    }
                    UserSingleton.riegos = riegos
                    riegoAdapter.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Log.e("RiegoFragment", "Error al obtener riegos", error)
            }
        )
        requestQueue.add(request)
    }

    private fun formatDateToTime(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return try {
            val date: Date = inputFormat.parse(dateString) ?: return dateString
            outputFormat.format(date)
        } catch (e: ParseException) {
            dateString
        }
    }


    private fun formatDiasRiego(dias: List<String>): String {
        return when {
            dias.size == 7 -> "Todos los días"
            dias.containsAll(listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")) &&
                    !dias.containsAll(listOf("Sábado", "Domingo")) -> "Lunes a Viernes"
            else -> dias.joinToString(", ")
        }
    }

    private fun toggleRiego(riego: Riego) {
        val url = "http://192.168.1.23:4000/riegos/${riego.id}"
        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonParams = JSONObject().apply {
            put("activo", riego.activo)
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, jsonParams,
            Response.Listener { response ->
                Log.d("RiegoFragment", "Riego actualizado: $response")
            },
            Response.ErrorListener { error ->
                Log.e("RiegoFragment", "Error al actualizar riego", error)
            }
        )

        requestQueue.add(request)
    }

    private fun guardarRiego(dialog: Dialog) {
        val url = "http://192.168.1.23:4000/riegos"

        val horas = numPickerMin.value
        val minutos = numPickerSeg.value
        val amPm = numPickerAm.displayedValues[numPickerAm.value]

        Log.d("Fecha", "$horas:$minutos $amPm")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City")).apply {
            set(Calendar.HOUR, horas)
            set(Calendar.MINUTE, minutos)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.AM_PM, if (amPm == "AM") Calendar.PM else Calendar.AM)
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val isoDate = inputFormat.format(calendar.time)
        val dias = mutableListOf<String>()

        if (dialog.findViewById<MaterialCheckBox>(R.id.btnD).isChecked) dias.add("Domingo")
        if (dialog.findViewById<MaterialCheckBox>(R.id.btnL).isChecked) dias.add("Lunes")
        if (dialog.findViewById<MaterialCheckBox>(R.id.btnM).isChecked) dias.add("Martes")
        if (dialog.findViewById<MaterialCheckBox>(R.id.btnX).isChecked) dias.add("Miércoles")
        if (dialog.findViewById<MaterialCheckBox>(R.id.btnJ).isChecked) dias.add("Jueves")
        if (dialog.findViewById<MaterialCheckBox>(R.id.btnV).isChecked) dias.add("Viernes")
        if (dialog.findViewById<MaterialCheckBox>(R.id.btnS).isChecked) dias.add("Sabado")

        // Crea el objeto JSON para enviar
        val jsonParams = JSONObject().apply {
            put("id_usuario", UserSingleton.id)
            put("hora_riego", isoDate)
            put("dias_riego", JSONArray(dias))
            put("activo", true)
        }

        val requestQueue = Volley.newRequestQueue(requireContext())

        val request = object : JsonObjectRequest(
            Method.POST, url, jsonParams,
            Response.Listener { response ->
                Log.d("RecordatorioFragment", "Recordatorio creado: $response")
                dialog.dismiss()
                fetchRiegos() // Actualiza la lista de recordatorios
            },
            Response.ErrorListener { error ->
                Log.e("RecordatorioFragment", "Error al crear el recordatorio", error)
            }
        ) {
            // Redefine el método para obtener cabeceras
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(request)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RiegoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RiegoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}