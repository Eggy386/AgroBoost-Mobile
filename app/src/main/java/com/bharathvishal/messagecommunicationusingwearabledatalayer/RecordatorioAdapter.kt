// RecordatorioAdapter.kt
package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordatorioAdapter(
    private var recordatorios: List<Recordatorio>,
    private val actualizarEstado: (String, Boolean) -> Unit
) : RecyclerView.Adapter<RecordatorioAdapter.RecordatorioViewHolder>() {

    inner class RecordatorioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val switchActivo: Switch = itemView.findViewById(R.id.switchRecordatorio)

        fun bind(recordatorio: Recordatorio) {
            itemView.findViewById<TextView>(R.id.textViewNombreRecordatorio).text = recordatorio.nombre
            itemView.findViewById<TextView>(R.id.textViewDiasRecordatorio).text = recordatorio.dias
            itemView.findViewById<TextView>(R.id.textViewHoraRecordatorio).text = recordatorio.hora
            switchActivo.isChecked = recordatorio.activo

            switchActivo.setOnCheckedChangeListener { _, isChecked ->
                actualizarEstado(recordatorio.id, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordatorioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recordatorio, parent, false)
        return RecordatorioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecordatorioViewHolder, position: Int) {
        holder.bind(recordatorios[position])
    }

    override fun getItemCount(): Int {
        return recordatorios.size
    }

    // Método para actualizar los recordatorios en el adaptador
    fun updateRecordatorios(newRecordatorios: List<Recordatorio>) {
        recordatorios = newRecordatorios
        notifyDataSetChanged()
    }
}