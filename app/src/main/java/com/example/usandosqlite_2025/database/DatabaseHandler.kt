package com.example.usandosqlite_2025.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.usandosqlite_2025.entity.Cadastro
import java.util.ArrayList

class DatabaseHandler private constructor(context: Context) : SQLiteOpenHelper(
    context.applicationContext,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "bdfile.sqlite"
        const val TABLE_NAME = "cadastro"
        const val COLUMN_ID = "_id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_TELEFONE = "telefone"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(context: Context): DatabaseHandler {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHandler(context)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(banco: SQLiteDatabase?) {
        banco?.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NOME TEXT, $COLUMN_TELEFONE TEXT)"
        )
    }

    override fun onUpgrade(banco: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        banco?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(banco)
    }

    fun inserir(cadastro: Cadastro) {
        val db = writableDatabase
        val registro = ContentValues().apply {
            put(COLUMN_NOME, cadastro.nome)
            put(COLUMN_TELEFONE, cadastro.telefone)
        }
        db.insert(TABLE_NAME, null, registro)
    }

    fun alterar(cadastro: Cadastro) {
        val db = writableDatabase
        val registro = ContentValues().apply {
            put(COLUMN_NOME, cadastro.nome)
            put(COLUMN_TELEFONE, cadastro.telefone)
        }
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(cadastro._id.toString())
        db.update(TABLE_NAME, registro, selection, selectionArgs)
    }

    fun excluir(id: Int) {
        val db = writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        db.delete(TABLE_NAME, selection, selectionArgs)
    }

    fun pesquisar(id: Int): Cadastro? {
        val db = readableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(
            TABLE_NAME, null, selection, selectionArgs, null, null, null
        )

        var retorno: Cadastro? = null
        cursor.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(COLUMN_ID)
                val nomeIndex = it.getColumnIndex(COLUMN_NOME)
                val telefoneIndex = it.getColumnIndex(COLUMN_TELEFONE)

                if (idIndex != -1 && nomeIndex != -1 && telefoneIndex != -1) {
                    val idValue = it.getInt(idIndex)
                    val nome = it.getString(nomeIndex)
                    val telefone = it.getString(telefoneIndex)
                    retorno = Cadastro(idValue, nome, telefone)
                }
            }
        }
        return retorno
    }

    fun listar(): ArrayList<Cadastro> {
        val cadastros = ArrayList<Cadastro>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        cursor.use {
            while (it.moveToNext()) {
                val idIndex = it.getColumnIndex(COLUMN_ID)
                val nomeIndex = it.getColumnIndex(COLUMN_NOME)
                val telefoneIndex = it.getColumnIndex(COLUMN_TELEFONE)

                if (idIndex != -1 && nomeIndex != -1 && telefoneIndex != -1) {
                    val id = it.getInt(idIndex)
                    val nome = it.getString(nomeIndex)
                    val telefone = it.getString(telefoneIndex)
                    cadastros.add(Cadastro(id, nome, telefone))
                }
            }
        }
        return cadastros
    }
}
