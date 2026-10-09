package com.livepoll.mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.livepoll.mobile.R
import com.livepoll.mobile.api.EnqueteResponse

class EnqueteAdapter(
    private val enquetes: List<EnqueteResponse>
): RecyclerView.Adapter<EnqueteAdapter.EnqueteViewHolder>() {

    class EnqueteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvTituloEnquete: TextView = itemView.findViewById(R.id.tvTituloEnquete)
        val tvStatusEnquete: TextView = itemView.findViewById(R.id.tvStatusEnquete)
        val tvQuantidadeOpcoes: TextView = itemView.findViewById(R.id.tvQuantidadeOpcoes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnqueteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_enquete, parent, false)

        return EnqueteViewHolder(view)
    }

    override fun onBindViewHolder(holder: EnqueteViewHolder, position: Int) {
        val enquete = enquetes[position]

        holder.tvTituloEnquete.text = enquete.titulo

        holder.tvStatusEnquete.text = if (enquete.aberta) "Status: Aberta" else "Status: Fechada"

        holder.tvQuantidadeOpcoes.text = "Opções: ${enquete.opcoes.size}"
    }

    override fun getItemCount(): Int = enquetes.size
}