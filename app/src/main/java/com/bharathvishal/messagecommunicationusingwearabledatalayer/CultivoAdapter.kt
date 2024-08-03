package com.bharathvishal.messagecommunicationusingwearabledatalayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CultivoAdapter(private val cultivos: List<Cultivo>) : RecyclerView.Adapter<CultivoAdapter.CultivoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CultivoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cultivo, parent, false)
        return CultivoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CultivoViewHolder, position: Int) {
        val cultivo = cultivos[position]
        holder.bind(cultivo)
    }

    override fun getItemCount(): Int = cultivos.size

    class CultivoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTipoCultivo: TextView = itemView.findViewById(R.id.textTipoCultivo)
        private val imageCultivo: ImageView = itemView.findViewById(R.id.imageCultivo)
        private val textTemperaturaCultivo: TextView = itemView.findViewById(R.id.textTemperaturaCultivo)
        private val textHumedadCultivo: TextView = itemView.findViewById(R.id.textHumedadCultivo)
        // Otras vistas...

        fun bind(cultivo: Cultivo) {
            textTipoCultivo.text = cultivo.tipoCultivo
            // Set the image based on the tipo_cultivo

            if (cultivo.tipoCultivo == "Maíz") {
                imageCultivo.setImageResource(R.mipmap.maiz)
            } else {
                imageCultivo.setImageResource(R.mipmap.frambuesa)
            }
        }
    }
}

