package com.example.usandosqlite_2025.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.usandosqlite_2025.R
import com.example.usandosqlite_2025.database.DatabaseHandler
import com.example.usandosqlite_2025.entity.Cadastro

class MeuAdapter(val context: Context, val cursor: Cursor): BaseAdapter() {

    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any? {
        cursor.moveToPosition(position)

        val cadastro = Cadastro(
            cursor.getInt(DatabaseHandler.COLUMN_ID.toInt()),
            cursor.getString(DatabaseHandler.COLUMN_NOME.toInt()),
            cursor.getString(DatabaseHandler.COLUMN_TELEFONE.toInt())
        )

        return cadastro
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getInt(DatabaseHandler.COLUMN_ID.toInt()).toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View? {

        //recupera a instancia do nosso elemento lista (container com dados de cada elemento da lista)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = inflater.inflate(R.layout.elemento_lista, null)

        //recupero os componentes visuais da tela
        val tvNomeElementoLista  = v.findViewById<TextView>(R.id.tvNomeElementoLista)
        val tvTelefoneElementoLista = v.findViewById<TextView>(R.id.tvTelefoneElementoLista)

        cursor.moveToPosition(position)
        tvNomeElementoLista.text = cursor.getString(DatabaseHandler.COLUMN_NOME.toInt())
        tvTelefoneElementoLista.text = cursor.getString(DatabaseHandler.COLUMN_TELEFONE.toInt())

        return v

    }
}