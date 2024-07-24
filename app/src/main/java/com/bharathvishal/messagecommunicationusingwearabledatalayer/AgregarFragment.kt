package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import java.util.Calendar

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
                    val selectedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
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