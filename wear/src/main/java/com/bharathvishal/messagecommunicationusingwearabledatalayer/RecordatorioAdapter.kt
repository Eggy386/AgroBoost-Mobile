package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial

class RecordatorioAdapter(
    private var recordatorioList: List<RecordatorioData>
) : RecyclerView.Adapter<RecordatorioAdapter.RecordatorioViewHolder>() {

    inner class RecordatorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.textViewRecordatorioTitulo)
        val frecuencia: TextView = itemView.findViewById(R.id.textViewNextRecordatorio)
        val hora: TextView = itemView.findViewById(R.id.textViewHoraRecordatorio)
        val switch: SwitchMaterial = itemView.findViewById(R.id.switchRecordatorio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordatorioViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recordatorio_item, parent, false)
        return RecordatorioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordatorioViewHolder, position: Int) {
        var recordatorio = recordatorioList[position]
        holder.titulo.text = recordatorio.nombre
        holder.frecuencia.text = recordatorio.dias
        holder.hora.text = recordatorio.hora
        holder.switch.isChecked = recordatorio.activo
    }

    override fun getItemCount() = recordatorioList.size
}
