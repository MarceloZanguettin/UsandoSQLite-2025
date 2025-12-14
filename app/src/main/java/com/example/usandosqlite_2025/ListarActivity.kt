package com.example.usandosqlite_2025

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.usandosqlite_2025.adapter.MeuAdapter
import com.example.usandosqlite_2025.database.DatabaseHandler
import com.example.usandosqlite_2025.databinding.ActivityListarBinding

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var banco: DatabaseHandler
    private lateinit var adapter: MeuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)

        initList()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        initList() // Atualiza a lista quando a activity é retomada
    }

    private fun initList() {
        val cadastros = banco.listar()
        adapter = MeuAdapter(this, cadastros)
        binding.lvRegistros.adapter = adapter
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    fun fabIncluirOnClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}