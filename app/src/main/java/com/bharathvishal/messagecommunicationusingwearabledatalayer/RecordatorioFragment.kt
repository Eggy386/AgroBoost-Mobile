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
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecordatorioFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var numPickerMin: NumberPicker
    private lateinit var numPickerSeg: NumberPicker
    private lateinit var numPickerAm: NumberPicker
    private var selectedDays: Int = 0
    private lateinit var recyclerViewRecordatorios: RecyclerView

    private lateinit var adapter: RecordatorioAdapter

    private lateinit var layoutNoRecordatorio: LinearLayout
    private lateinit var layoutConRecordatorio: ScrollView

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
        val view = inflater.inflate(R.layout.fragment_recordatorio, container, false)

        val fab = view.findViewById<FloatingActionButton>(R.id.addRecordatorio)
        fab.setOnClickListener {
            showBottomDialog()
        }

        // Inicializa el adaptador y pasa la referencia a la función
        val recordatorios = mutableListOf<Recordatorio>() // Tu lista de recordatorios vacía

        adapter = RecordatorioAdapter(recordatorios) { recordatorioId, activo ->
            actualizarEstadoRecordatorio(recordatorioId, activo)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRecordatorios)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutNoRecordatorio = view.findViewById(R.id.layoutNoRecordatorio)
        layoutConRecordatorio = view.findViewById(R.id.layoutConRecordatorio)

        recyclerViewRecordatorios = view.findViewById(R.id.recyclerViewRecordatorios)
        recyclerViewRecordatorios.layoutManager = LinearLayoutManager(requireContext())

        fetchRecordatorios()
    }

    private fun fetchRecordatorios() {
        val userId = UserSingleton.id
        val url = "http://192.168.1.23:4000/recordatorio/$userId"

        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                Log.v("Recordatorios", "$response")
                if (response.length() == 0) {
                    layoutNoRecordatorio.visibility = View.VISIBLE
                    layoutConRecordatorio.visibility = View.GONE
                } else {
                    layoutNoRecordatorio.visibility = View.GONE
                    layoutConRecordatorio.visibility = View.VISIBLE
                    val recordatorios = mutableListOf<Recordatorio>()
                    for (i in 0 until response.length()) {
                        val recordatorioJson = response.getJSONObject(i)
                        try {
                            val id = recordatorioJson.getString("_id")
                            val nombre = recordatorioJson.getString("nombre_recordatorio")
                            val hora = recordatorioJson.getString("hora_recordatorio")
                            val diasArray = recordatorioJson.getJSONArray("dias_recordatorio")
                            val activo = recordatorioJson.getBoolean("activo")

                            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                            val date = inputFormat.parse(hora)
                            val horaFormateada = outputFormat.format(date)

                            val diasList = mutableListOf<String>()
                            for (j in 0 until diasArray.length()) {
                                diasList.add(diasArray.getString(j))
                            }
                            val dias = diasList.joinToString(", ")

                            val recordatorio = Recordatorio(id, nombre, dias, horaFormateada, activo)
                            recordatorios.add(recordatorio)
                        } catch (e: JSONException) {
                            Log.e("RecordatorioFragment", "Error parsing recordatorio JSON", e)
                        }
                    }
                    // Actualiza el adaptador con los nuevos datos
                    adapter.updateRecordatorios(recordatorios)
                }
            },
            Response.ErrorListener { error ->
                Log.e("RecordatorioFragment", "Error fetching recordatorios", error)
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun actualizarEstadoRecordatorio(recordatorioId: String, activo: Boolean) {
        val url = "http://192.168.1.23:4000/recordatorio/$recordatorioId"

        val requestQueue = Volley.newRequestQueue(requireContext())

        val requestBody = JSONObject().apply {
            put("activo", activo)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, requestBody,
            Response.Listener { response ->
                Log.d("RecordatorioFragment", "Recordatorio actualizado: $response")
            },
            Response.ErrorListener { error ->
                Log.e("RecordatorioFragment", "Error al actualizar el recordatorio", error)
            }
        )

        requestQueue.add(jsonObjectRequest)
    }


    private fun showBottomDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheet_layout_recordatorio)

        val cancelButton = dialog.findViewById<Button>(R.id.buttonCancelRecordatorio)
        cancelButton.setOnClickListener { dialog.dismiss() }

        numPickerMin = dialog.findViewById(R.id.numPickerMinRec)
        numPickerSeg = dialog.findViewById(R.id.numPickerSegRec)
        numPickerAm = dialog.findViewById(R.id.numPickerAmRec)

        numPickerMin.minValue = 0
        numPickerMin.maxValue = 12

        numPickerSeg.minValue = 0
        numPickerSeg.maxValue = 59

        val str = arrayOf("AM", "PM")
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecordatorioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
