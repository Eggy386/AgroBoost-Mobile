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
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

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

        circularProgressBarTemperatura = view.findViewById(R.id.circularProgressBarTemperatura)
        textViewTemperaturaProgress = view.findViewById(R.id.textViewTemperaturaProgress)

        circularProgressBarNitrogeno = view.findViewById(R.id.circularProgressBarNitrogeno)
        textViewNitrogenoProgress = view.findViewById(R.id.textViewNitrogenoProgress)

        circularProgressBarFosforo = view.findViewById(R.id.circularProgressBarFosforo)
        textViewFosforoProgress = view.findViewById(R.id.textViewFosforoProgress)

        circularProgressBarPotasio = view.findViewById(R.id.circularProgressBarPotasio)
        textViewPotasioProgress = view.findViewById(R.id.textViewPotasioProgress)

        // Llamar a la función para obtener los datos del dispositivo
        obtenerDatosDispositivo()
    }

    private fun obtenerDatosDispositivo() {
        val userId = UserSingleton.id // Asegúrate de que UserSingleton está correctamente importado
        val url = "http://192.168.1.23:4000/dispositivo/$userId"

        val requestQueue = Volley.newRequestQueue(requireContext())

        val jsonArrayRequest = JsonArrayRequest(
            url,
            Response.Listener { response ->
                try {
                    for (i in 0 until response.length()) {
                        val dispositivo = response.getJSONObject(i)
                        actualizarGraficas(dispositivo)
                    }
                } catch (e: Exception) {
                    Log.e("HomeFragment", "Error al procesar la respuesta JSON", e)
                }
            },
            Response.ErrorListener { error ->
                Log.e("HomeFragment", "Error en la solicitud: ${error.message}")
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun actualizarGraficas(dispositivo: JSONObject) {
        // Verificar si el dispositivo tiene datos de humedad y temperatura como arrays
        if (dispositivo.has("datos")) {
            val datos = dispositivo.get("datos")
            if (datos is JSONArray) {
                // Verificar si el dispositivo es de tipo Humedad o Temperatura y extraer el valor
                when (dispositivo.getString("nombre_dispositivo")) {
                    "Sensor de Humedad" -> {
                        val humedad = datos.getDouble(datos.length() - 1).toFloat()
                        circularProgressBarHumedad.apply {
                            progress = humedad
                            textViewHumedadProgress.text = "$humedad%"
                            progressMax = 100f
                            progressBarWidth = 15f
                            backgroundProgressBarWidth = 5f
                            backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                            progressBarColor = ContextCompat.getColor(requireContext(), R.color.blue)
                            startAngle = 0f
                        }
                    }
                    "Sensor de Temperatura" -> {
                        val temperatura = datos.getDouble(datos.length() - 1).toFloat()
                        circularProgressBarTemperatura.apply {
                            progress = temperatura
                            textViewTemperaturaProgress.text = "$temperatura°C"
                            progressMax = 40f
                            progressBarWidth = 15f
                            backgroundProgressBarWidth = 5f
                            backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                            progressBarColor = ContextCompat.getColor(requireContext(), R.color.red)
                            startAngle = 0f
                        }
                    }
                }
            } else if (datos is JSONObject) {
                // Verificar si el dispositivo es de tipo Nutrientes y extraer los valores
                when (dispositivo.getString("nombre_dispositivo")) {
                    "Sensor de Nutrientes" -> {
                        if (datos.has("nitrogeno")) {
                            val nitrogeno = datos.getJSONArray("nitrogeno").getDouble(datos.getJSONArray("nitrogeno").length() - 1).toFloat()
                            circularProgressBarNitrogeno.apply {
                                progress = nitrogeno
                                textViewNitrogenoProgress.text = "$nitrogeno PPM"
                                progressMax = 200f
                                progressBarWidth = 10f
                                backgroundProgressBarWidth = 5f
                                backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                                progressBarColor = ContextCompat.getColor(requireContext(), R.color.red)
                                startAngle = 0f
                            }
                        }
                        if (datos.has("fosforo")) {
                            val fosforo = datos.getJSONArray("fosforo").getDouble(datos.getJSONArray("fosforo").length() - 1).toFloat()
                            circularProgressBarFosforo.apply {
                                progress = fosforo
                                textViewFosforoProgress.text = "$fosforo PPM"
                                progressMax = 200f
                                progressBarWidth = 10f
                                backgroundProgressBarWidth = 5f
                                backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                                progressBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                                startAngle = 0f
                            }
                        }
                        if (datos.has("potasio")) {
                            val potasio = datos.getJSONArray("potasio").getDouble(datos.getJSONArray("potasio").length() - 1).toFloat()
                            circularProgressBarPotasio.apply {
                                progress = potasio
                                textViewPotasioProgress.text = "$potasio PPM"
                                progressMax = 200f
                                progressBarWidth = 10f
                                backgroundProgressBarWidth = 5f
                                backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                                progressBarColor = ContextCompat.getColor(requireContext(), R.color.blue)
                                startAngle = 0f
                            }
                        }
                    }
                }
            }
        }
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