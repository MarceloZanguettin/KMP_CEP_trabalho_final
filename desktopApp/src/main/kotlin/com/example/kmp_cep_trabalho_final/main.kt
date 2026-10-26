package com.example.kmp_cep_trabalho_final

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.kmp_cep_trabalho_final.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMP_CEP_trabalho_final",
    ) {
        App()
    }
}