
package com.dannyrodrygues.printworth.presentation.calculator

import com.dannyrodrygues.printworth.domain.model.Money
import com.dannyrodrygues.printworth.domain.model.MoneyRate
import com.dannyrodrygues.printworth.domain.model.Printer
import com.dannyrodrygues.printworth.domain.model.PrintCalculationInput
import com.dannyrodrygues.printworth.domain.model.PricingMethod

/**
 * Identifica os campos para apresentação dos erros.
 */
enum class CalculatorFormField {
    FILAMENT_WEIGHT,
    FILAMENT_PRICE,
    PRINT_HOURS,
    PRINT_MINUTES,
    PRINTER,
    AVERAGE_POWER,
    ELECTRICITY_RATE,
    PRINTER_WEAR,
    PACKAGING,
    PROFIT_MARGIN,
    COMMISSION
}

/**
 * Resultado da validação do formulário.
 */
sealed interface CalculatorFormValidationResult {

    data class Success(
        val input: PrintCalculationInput
    ) : CalculatorFormValidationResult

    data class Failure(
        val errors: Map<CalculatorFormField, String>
    ) : CalculatorFormValidationResult
}

/**
 * Valida o formulário e converte seus valores
 * para os modelos da camada de domínio.
 *
 * Não executa cálculos financeiros.
 */
class CalculatorFormValidator {

    fun validate(
        state: CalculatorFormState,
        selectedPrinter: Printer?
    ): CalculatorFormValidationResult {

        val errors = mutableMapOf<CalculatorFormField, String>()

        val weight = parseNumber(
            state.filamentWeightGrams,
            2,
            "Peso do filamento",
            CalculatorFormField.FILAMENT_WEIGHT,
            errors,
            positive = true
        )

        val filamentPrice = parseNumber(
            state.filamentPricePerKg,
            4,
            "Preço do filamento",
            CalculatorFormField.FILAMENT_PRICE,
            errors
        )

        val hours = parseNumber(
            state.printHours,
            0,
            "Horas",
            CalculatorFormField.PRINT_HOURS,
            errors,
            maximum = Int.MAX_VALUE.toLong()
        )

        val minutes = parseNumber(
            state.printMinutes,
            0,
            "Minutos",
            CalculatorFormField.PRINT_MINUTES,
            errors,
            maximum = 59L
        )

        if (hours == 0L && minutes == 0L) {
            errors[CalculatorFormField.PRINT_HOURS] =
                "O tempo de impressão deve ser maior que zero."
        }


        if (
            selectedPrinter == null ||
            state.selectedPrinterId != selectedPrinter.id ||
            selectedPrinter.id.isBlank() ||
            selectedPrinter.name.isBlank()
        ) {
            errors[CalculatorFormField.PRINTER] =
                "Selecione uma impressora válida."
        }


        val power = parseNumber(
            state.averagePowerWatts,
            0,
            "Potência média",
            CalculatorFormField.AVERAGE_POWER,
            errors,
            positive = true,
            maximum = Int.MAX_VALUE.toLong()
        )

        val electricity = parseNumber(
            state.electricityRatePerKwh,
            4,
            "Tarifa de energia",
            CalculatorFormField.ELECTRICITY_RATE,
            errors
        )

        val wear = parseNumber(
            state.printerWearPerHour,
            4,
            "Desgaste da impressora",
            CalculatorFormField.PRINTER_WEAR,
            errors
        )

        val packaging = parseNumber(
            state.packagingCost,
            2,
            "Embalagem",
            CalculatorFormField.PACKAGING,
            errors,
            emptyIsZero = true
        )


        val margin = parseNumber(
            state.profitMarginPercent,
            2,
            "Lucro desejado",
            CalculatorFormField.PROFIT_MARGIN,
            errors,
            maximum = Int.MAX_VALUE.toLong()
        )


        val commission = if (state.commissionEnabled) {
            parseNumber(
                state.commissionPercent,
                2,
                "Comissão",
                CalculatorFormField.COMMISSION,
                errors,
                maximum = 9_999L
            )
        } else {
            0L
        }
// A margem sobre a venda e a comissão
// não podem somar 100% ou mais.
        if (
            state.pricingMethod == PricingMethod.SALE_MARGIN &&
            margin != null &&
            commission != null &&
            margin + commission >= 10_000L
        ) {
            errors[CalculatorFormField.PROFIT_MARGIN] =
                "A soma da margem sobre a venda e da " +
                        "comissão deve ser inferior a 100%."
        }


        if (errors.isNotEmpty()) {
            return CalculatorFormValidationResult.Failure(
                errors = errors.toMap()
            )
        }

        return CalculatorFormValidationResult.Success(
            input = PrintCalculationInput(
                pieceName = state.pieceName.trim()
                    .ifBlank { null },

                filamentWeightCentigrams = requireNotNull(weight),
                filamentPricePerKg = MoneyRate(
                    requireNotNull(filamentPrice)
                ),

                printHours = requireNotNull(hours).toInt(),
                printMinutes = requireNotNull(minutes).toInt(),


                printer = requireNotNull(selectedPrinter).copy(
                    averagePowerWatts = requireNotNull(power).toInt()
                ),

                averagePowerWatts = requireNotNull(power).toInt(),

                electricityRatePerKwh = MoneyRate(
                    requireNotNull(electricity)
                ),

                printerWearPerHour = MoneyRate(
                    requireNotNull(wear)
                ),

                packagingCost = Money(
                    requireNotNull(packaging)
                ),

                profitMarginBasisPoints =
                    requireNotNull(margin).toInt(),

                commissionEnabled = state.commissionEnabled,

                commissionBasisPoints =
                    requireNotNull(commission).toInt(),

                pricingMethod = state.pricingMethod
            )
        )
    }

    private fun parseNumber(
        rawValue: String,
        fractionDigits: Int,
        fieldName: String,
        field: CalculatorFormField,
        errors: MutableMap<CalculatorFormField, String>,
        positive: Boolean = false,
        maximum: Long = Long.MAX_VALUE,
        emptyIsZero: Boolean = false
    ): Long? {

        val value = if (emptyIsZero && rawValue.isBlank()) {
            "0"
        } else {
            rawValue
        }

        return when (
            val result = DecimalInputParser.parse(
                rawValue = value,
                fractionDigits = fractionDigits,
                fieldName = fieldName
            )
        ) {
            is DecimalParseResult.Error -> {
                errors[field] = result.message
                null
            }

            is DecimalParseResult.Success -> {
                val number = result.scaledValue

                when {
                    positive && number == 0L -> {
                        errors[field] =
                            "$fieldName deve ser maior que zero."
                        null
                    }

                    number > maximum -> {
                        errors[field] =
                            "$fieldName excede o limite permitido."
                        null
                    }

                    else -> number
                }
            }
        }
    }
}
