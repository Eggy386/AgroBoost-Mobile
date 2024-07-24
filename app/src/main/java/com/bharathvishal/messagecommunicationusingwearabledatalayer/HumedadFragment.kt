package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HumedadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HumedadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var lineChartHumedad: LineChart

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
        return inflater.inflate(R.layout.fragment_humedad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineChartHumedad = view.findViewById(R.id.lineCharHumedad)

        setupLineChart(lineChartHumedad, "Humedad", arrayOf("2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00"), 0f, 100f)

        setDataHumedad()
    }

    private fun setupLineChart(lineChart: LineChart, label: String, xAxisLabels: Array<String>, yAxisMin: Float, yAxisMax: Float) {
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.setBackgroundColor(Color.parseColor("#F7F7F7"))

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.parseColor("#5C5C5C")
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)

        val yAxisLeft: YAxis = lineChart.axisLeft
        yAxisLeft.setDrawGridLines(true)
        yAxisLeft.gridColor = Color.DKGRAY
        yAxisLeft.textColor = Color.parseColor("#5C5C5C")
        yAxisLeft.axisMinimum = yAxisMin
        yAxisLeft.axisMaximum = yAxisMax

        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false
    }

    private fun setDataHumedad() {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, 80f))
        entries.add(Entry(1f, 75f))
        entries.add(Entry(2f, 52f))
        entries.add(Entry(3f, 48f))
        entries.add(Entry(4f, 35f))
        entries.add(Entry(5f, 43f))
        entries.add(Entry(6f, 60f))

        val lineDataSet = LineDataSet(entries, "Humedad")
        lineDataSet.color = Color.parseColor("#73CDFF")
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius = 6f
        lineDataSet.circleHoleRadius = 4f
        lineDataSet.setCircleColor(Color.parseColor("#73CDFF"))
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.lineWidth = 4f
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(lineDataSet)
        lineChartHumedad.data = lineData
        lineChartHumedad.invalidate() // refresh
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HumedadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HumedadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}