package com.example.kmp_cep_trabalho_final

import kotlinx.serialization.Serializable

@Serializable
data class CepResponse(
    val cep: String = "",
    val logradouro: String = "",
    val bairro: String = "",
    val localidade: String = "",
    val uf: String = "",
    val erro: Boolean? = false
)