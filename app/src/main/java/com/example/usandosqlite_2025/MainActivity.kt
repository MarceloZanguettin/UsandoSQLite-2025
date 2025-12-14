package com.example.usandosqlite_2025

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
import com.example.usandosqlite_2025.database.DatabaseHandler.Companion.COLUMN_NOME
import com.example.usandosqlite_2025.database.DatabaseHandler.Companion.COLUMN_TELEFONE
import com.example.usandosqlite_2025.databinding.ActivityMainBinding
import com.example.usandosqlite_2025.entity.Cadastro


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var banco: DatabaseHandler

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
        if (intent.getIntExtra("cod", 0) != 0) {
            binding.etCod.setText(intent.getIntExtra("cod", 0).toString())
            binding.etNome.setText(intent.getStringExtra("nome"))
            binding.etTelefone.setText(intent.getStringExtra("telefone"))

        } else {
            binding.btExcluir.visibility = View.GONE
            binding.btPesquisar.visibility = View.GONE

        }
    }


    fun btListarOnClick(view: View) {

        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }


//        //acesso ao banco de dados
//        val registros : Cursor = banco.listar()
//
//        //apresentação da devolutiva visual para o usuário
//        val saida = StringBuilder()
//
//
//        while ( registros.moveToNext() ) {
//            val nome : String? = registros.getString(DatabaseHandler.COLUMN_NOME.toInt())
//            val telefone : String? = registros.getString(COLUMN_TELEFONE.toInt())
//
//            saida.append( "${nome} - ${telefone}\n")
//
//        }
//
//        Toast.makeText(
//            this,
//            saida.toString(),
//            Toast.LENGTH_SHORT
//        ).show()




    fun btPesquisarOnClick(view: View) {

        //Validação dos campos de tela


        //acesso ao banco
        val etCodPesquisar = EditText(this)


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Digite o Código")
        builder.setView(etCodPesquisar)
        builder.setCancelable(false)
        builder.setNegativeButton(
            "Fechar",
            null
        )
        builder.setPositiveButton(
            "Pesquisar",
            { dialog, which ->
                val cadastro = banco.pesquisar(etCodPesquisar.text.toString().toInt())
                if ( cadastro != null ) {
                    binding.etCod.setText(etCodPesquisar.text.toString())
                    binding.etNome.setText(cadastro.nome)
                    binding.etTelefone.setText(cadastro.telefone)

                } else {

                    binding.etNome.setText("")
                    binding.etTelefone.setText("")

                    Toast.makeText(
                        this,
                        "Registro não encontrado.",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
        )
        builder.show()

    }
    fun btExcluirOnClick(view: View) {
        //Validação dos campos de tela

        //acesso ao banco de dados
        banco.excluir( binding.etCod.text.toString().toInt() )

        //Apresentação da devolutiva visual para o usuário
        Toast.makeText(
            this,
            "Exclusão efetuada com sucesso.",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
    fun btSalvarOnClick(view: View) {

        //Validação dos campos de tela

        //acesso ao banco de dados
        var msg = ""

        if ( binding.etCod.text.toString().isEmpty()) {
            val cadastro = Cadastro(
                0,
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()

            )

            //Inclusão no banco de dados

            banco.inserir(cadastro)
            msg = "Inclusão efetuada com sucesso."
        } else {
            val cadastro = Cadastro( binding.etCod.text.toString().toInt(),
                binding.etNome.text.toString(),
                binding.etTelefone.text.toString()
            )

            banco.alterar(cadastro)
            msg = "Alteração efetuada com sucesso."

        }


        //Apresentação da devolutiva visual para o usuário

        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}