package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial

class RiegoAdapter(
    private var riegoList: List<RiegoData>
) : RecyclerView.Adapter<RiegoAdapter.RiegoViewHolder>() {

    inner class RiegoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val horaRiego: TextView = view.findViewById(R.id.textViewHoraRiego)
        val diasRiego: TextView = view.findViewById(R.id.textViewNextRiego)
        val switchActivo: SwitchMaterial = view.findViewById(R.id.switchRiego)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiegoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.riego_item, parent, false)
        return RiegoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiegoViewHolder, position: Int) {
        var riego = riegoList[position]
        holder.horaRiego.text = riego.hora_riego
        holder.diasRiego.text = riego.dias_riego
        holder.switchActivo.isChecked = riego.activo
    }

    override fun getItemCount(): Int = riegoList.size
}
