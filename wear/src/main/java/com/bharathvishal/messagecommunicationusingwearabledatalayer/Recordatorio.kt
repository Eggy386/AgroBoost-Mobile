package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Recordatorio: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recordatorio)

        val recordatorioRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewRecordatorios)
        recordatorioRecyclerView.layoutManager = LinearLayoutManager(this)
        val recordatorioAdapter = RecordatorioAdapter(DataRepository.recordatorioList)
        recordatorioRecyclerView.adapter = recordatorioAdapter
    }
}