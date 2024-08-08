package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bharathvishal.messagecommunicationusingwearabledatalayer.Riego

class Riego: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.riego)

        val riegoRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewRiegos)
        riegoRecyclerView.layoutManager = LinearLayoutManager(this)
        val riegoAdapter = RiegoAdapter(DataRepository.riegoList)
        riegoRecyclerView.adapter = riegoAdapter
    }
}