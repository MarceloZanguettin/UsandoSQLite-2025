package com.example.usandosqlite_2025

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

    fun btIncluirOnClick(view: View) {

        //Validação dos campos de tela

        val cadastro = Cadastro(
            0,
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()

        )

        //Inclusão no banco de dados

        banco.inserir(cadastro)

        //apresentação da devolutiva visual para o usuário

        Toast.makeText(
            this,
            "Inclusão efetuada com sucesso.",
            Toast.LENGTH_SHORT
        ).show()
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
        val cadastro :Cadastro? = banco.pesquisar(binding.etCod.text.toString().toInt())

        if ( cadastro != null ) {
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
    }
    fun btAlterarOnClick(view: View) {

        //Validação dos campos de tela

        //acesso ao banco de dados
        val cadastro : Cadastro = Cadastro(
            binding.etCod.text.toString().toInt(),
            binding.etNome.text.toString(),
            binding.etTelefone.text.toString()

        )

        //Alteração no banco de dados

        banco.alterar(cadastro)


        //Apresentação da devolutiva visual para o usuário

        Toast.makeText(
            this,
            "Alteração efetuada com sucesso.",
            Toast.LENGTH_SHORT
        ).show()
    }
}