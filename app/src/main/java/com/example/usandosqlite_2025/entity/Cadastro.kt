package com.example.usandosqlite_2025.entity

import com.google.firebase.firestore.DocumentId

data class Cadastro(
    @DocumentId
    val _id: Int,
    val nome: String,
    val telefone: String
)
