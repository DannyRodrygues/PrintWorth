
package com.dannyrodrygues.printworth.presentation.calculator

import androidx.lifecycle.ViewModel
import com.dannyrodrygues.printworth.domain.model.Printer
import com.dannyrodrygues.printworth.domain.model.PrintCalculationResult
import com.dannyrodrygues.printworth.domain.usecase.CalculatePrintCostUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.dannyrodrygues.printworth.domain.model.PricingMethod

/**
 * ViewModel responsável pelo estado e pelas ações
 * do formulário da calculadora PrintWorth.
 *
 * As regras financeiras permanecem na camada de domínio.
 */
class CalculatorViewModel : ViewModel() {

    private val validator = CalculatorFormValidator()

    private val calculatePrintCostUseCase =
        CalculatePrintCostUseCase()

    private var selectedPrinter: Printer? = null

    // Dados preenchidos pelo usuário
    private val _formState = MutableStateFlow(
        CalculatorFormState()
    )

    val formState: StateFlow<CalculatorFormState> =
        _formState.asStateFlow()

    // Mensagens de erro de cada campo
    private val _fieldErrors = MutableStateFlow<
            Map<CalculatorFormField, String>
            >(emptyMap())

    val fieldErrors: StateFlow<
            Map<CalculatorFormField, String>
            > = _fieldErrors.asStateFlow()

    // Resultado do cálculo
    private val _calculationResult =
        MutableStateFlow<PrintCalculationResult?>(null)

    val calculationResult:
            StateFlow<PrintCalculationResult?> =
        _calculationResult.asStateFlow()

    // Erros gerais de processamento
    private val _generalError =
        MutableStateFlow<String?>(null)

    val generalError: StateFlow<String?> =
        _generalError.asStateFlow()

    /**
     * Atualiza um campo e invalida o resultado anterior.
     */
    private fun updateForm(
        field: CalculatorFormField? = null,
        transform: (CalculatorFormState) -> CalculatorFormState
    ) {
        _formState.update(transform)

        if (field != null) {
            _fieldErrors.update { errors ->
                errors - field
            }
        }

        _generalError.value = null
        _calculationResult.value = null
    }

    fun onPieceNameChanged(value: String) {
        updateForm {
            it.copy(pieceName = value)
        }
    }

    fun onFilamentWeightChanged(value: String) {
        updateForm(CalculatorFormField.FILAMENT_WEIGHT) {
            it.copy(filamentWeightGrams = value)
        }
    }

    fun onFilamentPriceChanged(value: String) {
        updateForm(CalculatorFormField.FILAMENT_PRICE) {
            it.copy(filamentPricePerKg = value)
        }
    }

    fun onPrintHoursChanged(value: String) {
        updateForm(CalculatorFormField.PRINT_HOURS) {
            it.copy(printHours = value)
        }
    }

    fun onPrintMinutesChanged(value: String) {
        updateForm(CalculatorFormField.PRINT_MINUTES) {
            it.copy(printMinutes = value)
        }

        // A duração total depende dos dois campos.
        _fieldErrors.update {
            it - CalculatorFormField.PRINT_HOURS
        }
    }

    /**
     * Recebe a impressora selecionada.
     *
     * Preenche automaticamente sua potência média,
     * permitindo que o usuário a ajuste depois.
     */
    fun onPrinterSelected(printer: Printer?) {
        selectedPrinter = printer

        updateForm(CalculatorFormField.PRINTER) {
            it.copy(
                selectedPrinterId = printer?.id,
                averagePowerWatts =
                    printer?.averagePowerWatts?.toString() ?: ""
            )
        }

        _fieldErrors.update {
            it - CalculatorFormField.AVERAGE_POWER
        }
    }

    fun onAveragePowerChanged(value: String) {
        updateForm(CalculatorFormField.AVERAGE_POWER) {
            it.copy(averagePowerWatts = value)
        }
    }

    fun onElectricityRateChanged(value: String) {
        updateForm(CalculatorFormField.ELECTRICITY_RATE) {
            it.copy(electricityRatePerKwh = value)
        }
    }

    fun onPrinterWearChanged(value: String) {
        updateForm(CalculatorFormField.PRINTER_WEAR) {
            it.copy(printerWearPerHour = value)
        }
    }

    fun onPackagingCostChanged(value: String) {
        updateForm(CalculatorFormField.PACKAGING) {
            it.copy(packagingCost = value)
        }
    }


    /**
     * Atualiza o método de precificação.
     *
     * Mantém os valores digitados e invalida
     * o resultado anterior.
     */
    fun onPricingMethodChanged(method: PricingMethod) {

        updateForm(CalculatorFormField.PROFIT_MARGIN) {
            it.copy(pricingMethod = method)
        }

        _fieldErrors.update { errors ->
            errors - CalculatorFormField.COMMISSION
        }
    }

    fun onProfitMarginChanged(value: String) {
        updateForm(CalculatorFormField.PROFIT_MARGIN) {
            it.copy(profitMarginPercent = value)
        }
    }

    fun onCommissionEnabledChanged(enabled: Boolean) {
        updateForm(CalculatorFormField.COMMISSION) {
            it.copy(commissionEnabled = enabled)
        }

        _fieldErrors.update {
            it - CalculatorFormField.PROFIT_MARGIN
        }
    }

    fun onCommissionPercentChanged(value: String) {
        updateForm(CalculatorFormField.COMMISSION) {
            it.copy(commissionPercent = value)
        }

        _fieldErrors.update {
            it - CalculatorFormField.PROFIT_MARGIN
        }
    }

    /**
     * Valida o formulário e calcula o preço da peça.
     *
     * Retorna true somente quando o cálculo
     * foi concluído com sucesso.
     *
     * Não apaga os dados digitados.
     */
    fun calculate(): Boolean {

        _fieldErrors.value = emptyMap()
        _generalError.value = null
        _calculationResult.value = null

        val validation = validator.validate(
            state = _formState.value,
            selectedPrinter = selectedPrinter
        )

        return when (validation) {

            is CalculatorFormValidationResult.Failure -> {
                _fieldErrors.value = validation.errors
                false
            }

            is CalculatorFormValidationResult.Success -> {
                try {
                    val result = calculatePrintCostUseCase(
                        validation.input
                    )

                    _calculationResult.value = result
                    true

                } catch (exception: IllegalArgumentException) {
                    _generalError.value =
                        exception.message
                            ?: "Não foi possível validar o cálculo."
                    false

                } catch (exception: ArithmeticException) {
                    _generalError.value =
                        "Os valores informados excedem " +
                                "os limites de cálculo. " +
                                "Revise os dados e tente novamente."
                    false
                }
            }
        }
    }

    /**
     * Limpa o formulário e todos os estados associados.
     */
    fun resetForm() {
        selectedPrinter = null
        _formState.value = CalculatorFormState()
        _fieldErrors.value = emptyMap()
        _generalError.value = null
        _calculationResult.value = null
    }
}
