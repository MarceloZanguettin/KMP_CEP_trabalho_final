package com.example.kmp_cep_trabalho_final

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform