package com.example.usandosqlite_2025.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.usandosqlite_2025.MainActivity
import com.example.usandosqlite_2025.R
import com.example.usandosqlite_2025.database.DatabaseHandler
import com.example.usandosqlite_2025.entity.Cadastro

class MeuAdapter(val context: Context, val registros: List<Cadastro>): BaseAdapter() {

    override fun getCount(): Int {
        return registros.size
    }

    override fun getItem(pos: Int): Any? {

        val cadastro = Cadastro(
            registros.get(pos)._id,
            registros.get(pos).nome,
            registros.get(pos).telefone
        )

        return cadastro
    }

    override fun getItemId(pos: Int): Long {
        return registros.get(pos)._id.toInt().toLong()
    }

    override fun getView(
        pos: Int,
        p1: View?,
        p2: ViewGroup?
    ): View? {

        //recupera a instancia do nosso elemento lista (container com dados de cada elemento da lista)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.elemento_lista, null)

        //recupera os componentes visuais da tela
        val tvNomeElementoLista = v.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista = v.findViewById<TextView>(R.id.tvTelefoneElementoLista)
        val btEditarElementoLista = v.findViewById<ImageButton>(R.id.btEditarElementoLista )

        tvNomeElementoLista.text = registros.get(pos).nome
        tvTelefoneElementoLista.text = registros.get(pos).telefone

        btEditarElementoLista.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)

            intent.putExtra("cod", registros.get(pos)._id)
            intent.putExtra("nome", registros.get(pos).nome)
            intent.putExtra( "telefone", registros.get(pos).telefone)

            context.startActivity(intent)
        }

        return v
    }

}