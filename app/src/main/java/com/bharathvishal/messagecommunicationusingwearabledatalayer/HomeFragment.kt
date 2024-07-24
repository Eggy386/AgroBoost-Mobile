package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var circularProgressBarHumedad: CircularProgressBar
    private lateinit var textViewHumedadProgress: TextView

    private lateinit var circularProgressBarTemperatura: CircularProgressBar
    private lateinit var textViewTemperaturaProgress: TextView

    private lateinit var circularProgressBarNitrogeno: CircularProgressBar
    private lateinit var textViewNitrogenoProgress: TextView

    private lateinit var circularProgressBarFosforo: CircularProgressBar
    private lateinit var textViewFosforoProgress: TextView

    private lateinit var circularProgressBarPotasio: CircularProgressBar
    private lateinit var textViewPotasioProgress: TextView

    private lateinit var layoutHumedad: LinearLayout
    private lateinit var layoutTemperatura: LinearLayout
    private lateinit var layoutNutrientes: LinearLayout

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura los OnClickListener para navegar a los fragmentos correspondientes
        layoutHumedad = view.findViewById(R.id.layoutHumedad)
        layoutHumedad.setOnClickListener {
            val navController = findNavController()
            Log.d("HomeFragment", "NavController: $navController")
            findNavController().navigate(R.id.action_homeFragment_to_humedadFragment)
        }

        layoutTemperatura = view.findViewById(R.id.layoutTemperatura)
        layoutTemperatura.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_temperaturaFragment)
        }

        layoutNutrientes = view.findViewById(R.id.layoutNutrientes)
        layoutNutrientes.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nutrientesFragment)
        }

        // Configurar CircularProgressBar para Humedad
        circularProgressBarHumedad = view.findViewById(R.id.circularProgressBarHumedad)
        textViewHumedadProgress = view.findViewById(R.id.textViewHumedadProgress)

        val humedadProgress = 92f

        circularProgressBarHumedad.apply {
            progress = humedadProgress
            progressMax = 100f
            progressBarWidth = 15f
            backgroundProgressBarWidth = 5f
            backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
            progressBarColor = ContextCompat.getColor(requireContext(), R.color.blue)
            roundBorder = true
            startAngle = 0f
        }

        // Actualizar el texto del TextView con el progreso
        textViewHumedadProgress.text = "${humedadProgress.toInt()}%"

        circularProgressBarTemperatura = view.findViewById(R.id.circularProgressBarTemperatura)
        textViewTemperaturaProgress = view.findViewById(R.id.textViewTemperaturaProgress)

        val temperaturaProgress = 26f

        circularProgressBarTemperatura.apply {
            progress = temperaturaProgress
            progressMax = 40f
            progressBarWidth = 15f
            backgroundProgressBarWidth = 5f
            backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
            progressBarColor = ContextCompat.getColor(requireContext(), R.color.red)
            roundBorder = true
            startAngle = 0f
        }

        // Actualizar el texto del TextView con el progreso
        textViewTemperaturaProgress.text = "${temperaturaProgress.toInt()}°C"


        circularProgressBarNitrogeno = view.findViewById(R.id.circularProgressBarNitrogeno)
        textViewNitrogenoProgress = view.findViewById(R.id.textViewNitrogenoProgress)
        val nitrogenoProgress = 82f

        circularProgressBarNitrogeno.apply {
            progress = nitrogenoProgress
            progressMax = 100f
            progressBarWidth = 10f
            backgroundProgressBarWidth = 5f
            backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
            progressBarColor = ContextCompat.getColor(requireContext(), R.color.red)
            roundBorder = true
            startAngle = 0f
        }

        // Actualizar el texto del TextView con el progreso
        textViewNitrogenoProgress.text = "${nitrogenoProgress.toInt()} PPM"

        circularProgressBarFosforo= view.findViewById(R.id.circularProgressBarFosforo)
        textViewFosforoProgress = view.findViewById(R.id.textViewFosforoProgress)
        val fosforoProgress = 10.20f

        circularProgressBarFosforo.apply {
            progress = fosforoProgress
            progressMax = 12f
            progressBarWidth = 10f
            backgroundProgressBarWidth = 5f
            backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
            progressBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
            roundBorder = true
            startAngle = 0f
        }

        // Actualizar el texto del TextView con el progreso
        textViewFosforoProgress.text = "${fosforoProgress.toInt()} PPM"

        circularProgressBarPotasio = view.findViewById(R.id.circularProgressBarPotasio)
        textViewPotasioProgress = view.findViewById(R.id.textViewPotasioProgress)
        val potasioProgress = 250f

        circularProgressBarPotasio.apply {
            progress = potasioProgress
            progressMax = 260f
            progressBarWidth = 10f
            backgroundProgressBarWidth = 5f
            backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
            progressBarColor = ContextCompat.getColor(requireContext(), R.color.blue)
            roundBorder = true
            startAngle = 0f
        }

        // Actualizar el texto del TextView con el progreso
        textViewPotasioProgress.text = "${potasioProgress.toInt()} PPM"
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}