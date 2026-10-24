
package com.dannyrodrygues.printworth.presentation.printer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.dannyrodrygues.printworth.domain.model.Printer
import com.dannyrodrygues.printworth.domain.repository.PrinterRepository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Gerencia as impressoras pessoais cadastradas.
 *
 * O catálogo de modelos pré-definidos permanece
 * separado, em PrinterCatalog.
 *
 * A persistência é delegada ao PrinterRepository.
 */
class PrinterManagementViewModel(
    private val repository: PrinterRepository
) : ViewModel() {

    private val _printers =
        MutableStateFlow<List<Printer>>(emptyList())

    val printers: StateFlow<List<Printer>> =
        _printers.asStateFlow()

    private val _isLoading = MutableStateFlow(true)

    val isLoading: StateFlow<Boolean> =
        _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)

    val isSaving: StateFlow<Boolean> =
        _isSaving.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)

    val error: StateFlow<String?> =
        _error.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)

    val message: StateFlow<String?> =
        _message.asStateFlow()

    init {
        observePersonalPrinters()
    }

    /**
     * Observa as impressoras armazenadas no celular.
     */
    private fun observePersonalPrinters() {
        viewModelScope.launch {
            repository.observePrinters()
                .catch { exception ->
                    if (exception is CancellationException) {
                        throw exception
                    }

                    _error.value =
                        "Não foi possível carregar as impressoras."

                    _isLoading.value = false
                }
                .collect { savedPrinters ->
                    _printers.value = savedPrinters
                    _isLoading.value = false
                }
        }
    }

    /**
     * Cadastra uma impressora pessoal.
     */
    fun addPrinter(
        name: String,
        averagePowerWatts: Int
    ) {
        executeOperation {
            val printer = repository.addPrinter(
                name = name.trim(),
                averagePowerWatts = averagePowerWatts
            )

            _message.value =
                "Impressora ${printer.name} cadastrada."
        }
    }

    /**
     * Atualiza uma impressora pessoal existente.
     */
    fun updatePrinter(
        printer: Printer
    ) {
        executeOperation {
            repository.updatePrinter(printer)

            _message.value =
                "Impressora atualizada com sucesso."
        }
    }

    /**
     * Exclui uma impressora pessoal.
     *
     * A confirmação de exclusão será apresentada
     * pela interface antes de chamar esta função.
     */
    fun deletePrinter(
        printerId: String
    ) {
        executeOperation {
            repository.deletePrinter(printerId)

            _message.value =
                "Impressora excluída com sucesso."
        }
    }

    /**
     * Executa operações de armazenamento
     * sem bloquear a interface.
     */
    private fun executeOperation(
        operation: suspend () -> Unit
    ) {
        if (_isSaving.value) return

        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null
            _message.value = null

            try {
                operation()

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                _error.value =
                    exception.message
                        ?: "Não foi possível concluir a operação."

            } finally {
                _isSaving.value = false
            }
        }
    }

    /**
     * Limpa mensagens após sua apresentação.
     */
    fun clearMessages() {
        _error.value = null
        _message.value = null
    }
}
