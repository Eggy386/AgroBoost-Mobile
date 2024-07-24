package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FertilizacionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FertilizacionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var editTextHora: EditText
    private lateinit var editTextFecha: EditText

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
        val view = inflater.inflate(R.layout.fragment_fertilizacion, container, false)

        // Obtener referencias a los spinners
        val spinnerTipo: Spinner = view.findViewById(R.id.spinnerTipo)
        val spinnerMetodo: Spinner = view.findViewById(R.id.spinnerMetodo)

        // Configurar adaptadores para cada spinner
        setupSpinner(spinnerTipo, R.array.tipo_agroquimico_options)
        setupSpinner(spinnerMetodo, R.array.metodo_aplicacion_options)

        // Configurar el DatePickerDialog
        editTextFecha = view.findViewById(R.id.editTextFechaAgro)
        editTextFecha.setOnClickListener {
            showDatePickerDialog(editTextFecha)
        }

        // Configurar el TimePickerDialog
        editTextHora = view.findViewById(R.id.editTextTimeFecha)
        editTextHora.setOnClickListener {
            showTimePickerDialog(editTextHora)
        }


        return view
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

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Formatear y mostrar la fecha seleccionada
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                editText.setText(dateFormat.format(selectedDate.time))
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                // Formatear y mostrar la hora seleccionada
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                selectedTime.set(Calendar.MINUTE, selectedMinute)
                editText.setText(timeFormat.format(selectedTime.time))
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FertilizacionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FertilizacionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}