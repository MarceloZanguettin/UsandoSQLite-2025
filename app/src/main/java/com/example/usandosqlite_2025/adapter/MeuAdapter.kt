package com.example.usandosqlite_2025.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import com.example.usandosqlite_2025.MainActivity
import com.example.usandosqlite_2025.R
import com.example.usandosqlite_2025.entity.Cadastro

class MeuAdapter(val context: Context, private var cadastros: List<Cadastro>) : BaseAdapter(), Filterable {

    private var filteredCadastros = cadastros

    override fun getCount(): Int {
        return filteredCadastros.size
    }

    override fun getItem(position: Int): Any {
        return filteredCadastros[position]
    }

    override fun getItemId(position: Int): Long {
        return filteredCadastros[position]._id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.elemento_lista, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val cadastro = filteredCadastros[position]
        holder.tvNomeElementoLista.text = cadastro.nome
        holder.tvTelefoneElementoLista.text = cadastro.telefone

        holder.btEditarElementoLista.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("cod", cadastro._id)
            intent.putExtra("nome", cadastro.nome)
            intent.putExtra("telefone", cadastro.telefone)
            context.startActivity(intent)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val tvNomeElementoLista: TextView = view.findViewById(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista: TextView = view.findViewById(R.id.tvTelefoneElementoLista)
        val btEditarElementoLista: ImageButton = view.findViewById(R.id.btEditarElementoLista)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredCadastros = if (charString.isEmpty()) {
                    cadastros
                } else {
                    cadastros.filter {
                        it.nome.contains(charString, true) || it.telefone.contains(charString, true)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredCadastros
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCadastros = results?.values as List<Cadastro>
                notifyDataSetChanged()
            }
        }
    }
}