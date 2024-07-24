package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class Cultivo: ComponentActivity() {

    private lateinit var lineChartTemperatura: LineChart
    private lateinit var lineChartHumedad: LineChart
    private lateinit var lineChartPH: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cultivo)

        lineChartTemperatura = findViewById(R.id.lineCharTemperatura)
        lineChartHumedad = findViewById(R.id.lineCharHumedad)
        lineChartPH = findViewById(R.id.lineCharPH)

        setupLineChart(lineChartTemperatura, "Temperatura", arrayOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"), -10f, 50f)
        setupLineChart(lineChartHumedad, "Humedad", arrayOf("2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00"), 0f, 100f)
        setupLineChart(lineChartPH, "PH", arrayOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"), 0f, 6f)

        setDataTemperatura()
        setDataHumedad()
        setDataPH()
    }

    private fun setupLineChart(lineChart: LineChart, label: String, xAxisLabels: Array<String>, yAxisMin: Float, yAxisMax: Float) {
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.setBackgroundColor(Color.BLACK)

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.LTGRAY
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)

        val yAxisLeft: YAxis = lineChart.axisLeft
        yAxisLeft.setDrawGridLines(true)
        yAxisLeft.gridColor = Color.DKGRAY
        yAxisLeft.textColor = Color.LTGRAY
        yAxisLeft.axisMinimum = yAxisMin
        yAxisLeft.axisMaximum = yAxisMax

        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.isEnabled = false
    }

    private fun setDataTemperatura() {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, 18f))
        entries.add(Entry(1f, 22f))
        entries.add(Entry(2f, 19f))
        entries.add(Entry(3f, 21f))
        entries.add(Entry(4f, 25f))
        entries.add(Entry(5f, 29f))
        entries.add(Entry(6f, 22f))

        val lineDataSet = LineDataSet(entries, "Temperatura")
        lineDataSet.color = Color.parseColor("#FA5805")
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius = 3f
        lineDataSet.circleHoleRadius = 1.5f
        lineDataSet.setCircleColor(Color.WHITE)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.lineWidth = 2f
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(lineDataSet)
        lineChartTemperatura.data = lineData
        lineChartTemperatura.invalidate() // refresh
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
        lineDataSet.circleRadius = 4f
        lineDataSet.circleHoleRadius = 2f
        lineDataSet.setCircleColor(Color.WHITE)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.lineWidth = 2f
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(lineDataSet)
        lineChartHumedad.data = lineData
        lineChartHumedad.invalidate() // refresh
    }

    private fun setDataPH() {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, 2.5f))
        entries.add(Entry(1f, 3f))
        entries.add(Entry(2f, 2.9f))
        entries.add(Entry(3f, 4.1f))
        entries.add(Entry(4f, 3.5f))
        entries.add(Entry(5f, 4f))
        entries.add(Entry(6f, 2.8f))

        val lineDataSet = LineDataSet(entries, "PH")
        lineDataSet.color = Color.parseColor("#00D56D")
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(true)
        lineDataSet.circleRadius = 4f
        lineDataSet.circleHoleRadius = 2f
        lineDataSet.setCircleColor(Color.WHITE)
        lineDataSet.setDrawHighlightIndicators(false)
        lineDataSet.lineWidth = 2f
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(lineDataSet)
        lineChartPH.data = lineData
        lineChartPH.invalidate() // refresh
    }
}