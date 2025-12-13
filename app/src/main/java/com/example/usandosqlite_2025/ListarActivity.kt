package com.example.usandosqlite_2025

import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.usandosqlite_2025.adapter.MeuAdapter
import com.example.usandosqlite_2025.database.DatabaseHandler
import com.example.usandosqlite_2025.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding

    private lateinit var banco: DatabaseHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvRegistros)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initList()
    }

    private fun initList() {

        val cursor : Cursor = banco.listar()

        //Chamada do adapter para incluir na interface grafica
        val adapter = MeuAdapter(this, cursor)


        binding.lvRegistros.adapter = adapter
    }
}