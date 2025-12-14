package com.example.usandosqlite_2025

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.usandosqlite_2025.database.DatabaseHandler
import com.example.usandosqlite_2025.databinding.ActivityMainBinding
import com.example.usandosqlite_2025.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var banco: DatabaseHandler

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        banco = DatabaseHandler.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        initView()
    }

    private fun initView() {
        val cod = intent.getIntExtra("cod", 0)
        if (cod != 0) {
            binding.etCod.setText(cod.toString())
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etTelefone.setText(intent.getStringExtra("telefone"))
            binding.btExcluir.visibility = View.VISIBLE
            binding.btPesquisar.visibility = View.GONE
        } else {
            binding.btExcluir.visibility = View.GONE
            binding.btPesquisar.visibility = View.VISIBLE
            binding.etCod.text.clear()
            binding.etNome.text.clear()
            binding.etTelefone.text.clear()
        }
    }

    fun btListarOnClick(view: View) {
        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

    fun btPesquisarOnClick(view: View) {

        val msg = StringBuilder()

        db.collection("cadastro")
            .get()
            .addOnSuccessListener { result ->
                val registros = result.toString()

                for (document in result) {
                    val registro = document.getString("nome")

                    msg.append(registro + "\n")
                }
                Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao buscar registros: ${e.message}", Toast.LENGTH_SHORT).show()
            }


//        val etCodPesquisar = EditText(this).apply {
//            inputType = android.text.InputType.TYPE_CLASS_NUMBER
//        }
//
//        AlertDialog.Builder(this)
//            .setTitle("Digite o Código")
//            .setView(etCodPesquisar)
//            .setCancelable(false)
//            .setNegativeButton("Fechar", null)
//            .setPositiveButton("Pesquisar") { _, _ ->
//                val cod = etCodPesquisar.text.toString().toIntOrNull()
//                if (cod == null) {
//                    Toast.makeText(this, "Por favor, insira um código válido.", Toast.LENGTH_SHORT).show()
//                    return@setPositiveButton
//                }
//
//                val cadastro = banco.pesquisar(cod)
//                if (cadastro != null) {
//                    binding.etCod.setText(cadastro._id.toString())
//                    binding.etNome.setText(cadastro.nome)
//                    binding.etTelefone.setText(cadastro.telefone)
//                    binding.btExcluir.visibility = View.VISIBLE
//                } else {
//                    binding.etCod.setText("")
//                    binding.etNome.setText("")
//                    binding.etTelefone.setText("")
//                    Toast.makeText(this, "Registro não encontrado.", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .show()
    }

    fun btExcluirOnClick(view: View) {
        val cod = binding.etCod.text.toString().toIntOrNull()
        if (cod == null) {
            Toast.makeText(this, "Código inválido para exclusão.", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir este registro?")
            .setPositiveButton("Sim") { _, _ ->
                banco.excluir(cod)
                Toast.makeText(this, "Exclusão efetuada com sucesso.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Não", null)
            .show()
    }

    fun btSalvarOnClick(view: View) {
        val nome = binding.etNome.text.toString()
        val telefone = binding.etTelefone.text.toString()

        if (nome.isBlank() || telefone.isBlank()) {
            Toast.makeText(this, "Nome e telefone são obrigatórios.", Toast.LENGTH_SHORT).show()
            return
        }

        val codStr = binding.etCod.text.toString()

        val cadastro = Cadastro(codStr.toInt(), nome, telefone)

        db.collection("cadastro")
            .document(binding.etCod.text.toString())
            .set(cadastro)
            .addOnSuccessListener {
                Toast.makeText(this, "Inclusão efetuada com sucesso.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Erro ao inserir no Firestore: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

//        var msg: String
//
//        if (codStr.isEmpty()) {
//
//            banco.inserir(cadastro)
//            msg = "Inclusão efetuada com sucesso."
//        } else {
//            val cod = codStr.toIntOrNull()
//            if (cod == null) {
//                Toast.makeText(this, "Código inválido para alteração.", Toast.LENGTH_SHORT).show()
//                return
//            }
//            val cadastro = Cadastro(cod, nome, telefone)
//            banco.alterar(cadastro)
//            msg = "Alteração efetuada com sucesso."
//        }
//
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        finish()
    }
}