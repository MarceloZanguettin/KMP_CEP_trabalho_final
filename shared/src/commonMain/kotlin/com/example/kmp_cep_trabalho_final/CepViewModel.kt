package com.example.kmp_cep_trabalho_final

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class CepUiState(
    val cepInput: String = "",
    val isLoading: Boolean = false,
    val result: CepResponse? = null,
    val errorMessage: String? = null
)

class CepViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CepUiState())
    val uiState: StateFlow<CepUiState> = _uiState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true})
        }
    }

    fun onCepChanged(newCep: String) {
        val filteredCep = newCep.filter { it.isDigit() }.take(8)
        _uiState.value = _uiState.value.copy(cepInput = filteredCep)
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun buscarCep() {
        val cep = _uiState.value.cepInput
        if (cep.length != 8) return

        viewModelScope.launch {
            // Limpa os resultados e indica carregamento
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                result = null,
                errorMessage = null
            )

            try {
                val response: CepResponse = httpClient.get("https://viacep.com.br/ws/$cep/json/").body()

                if (response.erro == true) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Ocorreu um erro ao consultar o CEP Aguarde um momento e tente novamente"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = response
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Ocorreu um erro ao consultar o CEP Aguarde um momento e tente novamente"
                )
            }
        }
    }
}
