package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var imageCultivo: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var cultivoAdapter: CultivoAdapter

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

        // Inicializar RecyclerView
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCultivo)
        recyclerView.layoutManager = LinearLayoutManager(context)

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
                Log.v("obtenerDatosDispositivo", "$response")
                try {
                    val dispositivos = mutableListOf<Dispositivo>()
                    for (i in 0 until response.length()) {
                        val dispositivoJson = response.getJSONObject(i)
                        val idCultivoJson = dispositivoJson.getJSONObject("id_cultivo")

                        val cultivo = Cultivo(
                            id = idCultivoJson.getString("_id"),
                            tipoCultivo = idCultivoJson.getString("tipo_cultivo"),
                            tipoRiego = idCultivoJson.getString("tipo_riego"),
                            programaRiego = idCultivoJson.getString("programa_riego"),
                            metodoFertilizacion = idCultivoJson.getString("metodo_fertilizacion"),
                            fechaFertilizacion = idCultivoJson.getString("fecha_fertilizacion"),
                            cantidadFertilizante = idCultivoJson.getInt("cantidad_fertilizante"),
                            controlPlagas = idCultivoJson.getString("control_plagas"),
                            tecnicaPolinizacion = idCultivoJson.getString("tecnica_polinizacion"),
                            medidasSiembra = idCultivoJson.getInt("medidas_siembra"),
                            fechaPrevista = idCultivoJson.getString("fecha_prevista"),
                            idUsuario = idCultivoJson.getString("id_usuario")
                        )
                        val dispositivo = Dispositivo(
                            id = dispositivoJson.getString("_id"),
                            nombreDispositivo = dispositivoJson.getString("nombre_dispositivo"),
                            datos = dispositivoJson.get("datos"),
                            idCultivo = cultivo,
                            idUsuario = dispositivoJson.getString("id_usuario")
                        )
                        dispositivos.add(dispositivo)
                    }
                    handleCultivos(dispositivos)
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

    fun handleCultivos(dispositivos: List<Dispositivo>) {
        val cultivos = dispositivos.map { it.idCultivo }.distinctBy { it.id }
        if (cultivos.isEmpty()) {
            view?.findViewById<LinearLayout>(R.id.layoutNoCultivo)?.visibility = View.VISIBLE
            view?.findViewById<LinearLayout>(R.id.layoutUnCultivo)?.visibility = View.GONE
            view?.findViewById<RecyclerView>(R.id.recyclerViewCultivo)?.visibility = View.GONE
        } else if (cultivos.size == 1) {
            view?.findViewById<LinearLayout>(R.id.layoutNoCultivo)?.visibility = View.GONE
            view?.findViewById<LinearLayout>(R.id.layoutUnCultivo)?.visibility = View.VISIBLE
            view?.findViewById<RecyclerView>(R.id.recyclerViewCultivo)?.visibility = View.GONE
            // Configurar los datos para un solo cultivo
            val cultivo = cultivos[0]
            val dispositivosCultivo = dispositivos.filter { it.idCultivo.id == cultivo.id }
            dispositivosCultivo.forEach { actualizarGraficas(it) }
        } else {
            view?.findViewById<LinearLayout>(R.id.layoutNoCultivo)?.visibility = View.GONE
            view?.findViewById<LinearLayout>(R.id.layoutUnCultivo)?.visibility = View.GONE
            view?.findViewById<RecyclerView>(R.id.recyclerViewCultivo)?.visibility = View.VISIBLE
            // Configurar el adaptador del RecyclerView con la lista de cultivos
            cultivoAdapter = CultivoAdapter(cultivos)
            recyclerView.adapter = cultivoAdapter
        }
    }

    private fun actualizarGraficas(dispositivo: Dispositivo) {
        val datos = dispositivo.datos
        when (dispositivo.nombreDispositivo) {
            "Sensor de Humedad" -> {
                if (datos is JSONArray) {
                    val humedad = datos.getDouble(datos.length() - 1).toFloat()
                    circularProgressBarHumedad.apply {
                        progress = humedad
                        textViewHumedadProgress.text = "$humedad%"
                        progressMax = 100f
                        progressBarWidth = 15f
                        backgroundProgressBarWidth = 7f
                        roundBorder = true
                        startAngle = 180f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        setProgressWithAnimation(humedad, 1000)
                        progressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                        progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                        ContextCompat.getColor(requireContext(), R.color.blue)
                        backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    }
                }
            }
            "Sensor de Temperatura" -> {
                if (datos is JSONArray) {
                    val temperatura = datos.getDouble(datos.length() - 1).toFloat()
                    circularProgressBarTemperatura.apply {
                        progress = temperatura
                        textViewTemperaturaProgress.text = "$temperatura°C"
                        progressMax = 50f
                        progressBarWidth = 15f
                        backgroundProgressBarWidth = 7f
                        roundBorder = true
                        startAngle = 180f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        setProgressWithAnimation(temperatura, 1000)
                        progressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                        progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                        backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.red)
                        backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    }
                }
            }
            "Sensor de Nutrientes" -> {
                if (datos is JSONObject) {
                    val nitrogeno = datos.getDouble("nitrogeno").toFloat()
                    val fosforo = datos.getDouble("fosforo").toFloat()
                    val potasio = datos.getDouble("potasio").toFloat()
                    circularProgressBarNitrogeno.apply {
                        progress = nitrogeno
                        textViewNitrogenoProgress.text = "$nitrogeno ppm"
                        progressMax = 100f
                        progressBarWidth = 15f
                        backgroundProgressBarWidth = 7f
                        roundBorder = true
                        startAngle = 180f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        setProgressWithAnimation(nitrogeno, 1000)
                        progressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                        progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                        backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.green)
                        backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    }
                    circularProgressBarFosforo.apply {
                        progress = fosforo
                        textViewFosforoProgress.text = "$fosforo ppm"
                        progressMax = 100f
                        progressBarWidth = 15f
                        backgroundProgressBarWidth = 7f
                        roundBorder = true
                        startAngle = 180f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        setProgressWithAnimation(fosforo, 1000)
                        progressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                        progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                        backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                        backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    }
                    circularProgressBarPotasio.apply {
                        progress = potasio
                        textViewPotasioProgress.text = "$potasio ppm"
                        progressMax = 100f
                        progressBarWidth = 15f
                        backgroundProgressBarWidth = 7f
                        roundBorder = true
                        startAngle = 180f
                        progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                        setProgressWithAnimation(potasio, 1000)
                        progressBarColor = ContextCompat.getColor(requireContext(), R.color.background)
                        progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                        backgroundProgressBarColor = ContextCompat.getColor(requireContext(), R.color.orange)
                        backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
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