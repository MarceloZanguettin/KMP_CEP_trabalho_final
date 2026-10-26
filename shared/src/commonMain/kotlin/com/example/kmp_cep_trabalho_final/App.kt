package com.example.kmp_cep_trabalho_final

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource

import kmp_cep_trabalho_final.shared.generated.resources.Res
import kmp_cep_trabalho_final.shared.generated.resources.compose_multiplatform

@Composable
fun App(viewModel: CepViewModel = viewModel { CepViewModel() }) {
    MaterialTheme {
        val uiState by viewModel.uiState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        // Exibe a Snackbar se ocorrer erro durante a busca
        LaunchedEffect(uiState.errorMessage) {
            uiState.errorMessage?.let { message ->
                snackbarHostState.showSnackbar(message)
                viewModel.clearErrorMessage()
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // OutlinedTextField para entrada de CEP
                OutlinedTextField(
                    value = uiState.cepInput,
                    onValueChange = { viewModel.onCepChanged(it) },
                    label = { Text("Digite o CEP") },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Botão habilitado apenas com 8 dígitos e fora de carregamento
                val isButtonEnabled = uiState.cepInput.length == 8 && !uiState.isLoading

                ElevatedButton(
                    onClick = { viewModel.buscarCep() },
                    enabled = isButtonEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Buscar")
                    }
                }

                // Exibição dos 5 campos de resultado
                val result = uiState.result
                Text(text = "CEP: ${result?.cep ?: ""}")
                Text(text = "Logradouro: ${result?.logradouro ?: ""}")
                Text(text = "Bairro: ${result?.bairro ?: ""}")
                Text(text = "Localidade: ${result?.localidade ?: ""}")
                Text(text = "UF: ${result?.uf ?: ""}")
            }
        }
    }
}