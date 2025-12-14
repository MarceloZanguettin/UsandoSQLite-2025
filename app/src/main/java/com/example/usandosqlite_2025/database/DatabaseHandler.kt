package com.example.usandosqlite_2025.database

import com.example.usandosqlite_2025.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class DatabaseHandler ()  {

    private val firestore = Firebase.firestore

    companion object {
        private const val COLLECTION_NAME = "cadastro"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getInstance(): DatabaseHandler {
            return INSTANCE ?: synchronized(this) {
                val instance = DatabaseHandler()
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun inserir(cadastro: Cadastro) {

        firestore.collection(COLLECTION_NAME)
            .document(cadastro._id.toString())
            .set(cadastro).await()

    }

    suspend fun alterar(cadastro: Cadastro) {
        firestore.collection(COLLECTION_NAME)
            .document(cadastro._id.toString())
            .set(cadastro).await()
    }

    suspend fun excluir(id:Int) {
        firestore.collection(COLLECTION_NAME)
            .document(id.toString())
            .delete().await()
    }

    suspend fun pesquisar(id:Int): Cadastro? {
        val document =
            firestore
                .collection(COLLECTION_NAME)
                .document(id.toString())
                .get()
                .await()

        return document.toObject(Cadastro::class.java)

    }


    suspend fun listar(): List<Cadastro> {
        return listar("")
    }

    suspend fun listar(filtro: String): List<Cadastro> {

        val query = firestore
            .collection(COLLECTION_NAME)
        val snapshot = query.get().await()

        val cadastro = snapshot.toObjects<Cadastro>()

        return if ( filtro.isNotEmpty() ) {
            cadastro.filter { it.nome.contains(filtro) }
        } else {
            cadastro
        }
    }

}
