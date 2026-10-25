
package com.dannyrodrygues.printworth.presentation.calculator

import com.dannyrodrygues.printworth.domain.model.CalculationCurrency
import com.dannyrodrygues.printworth.domain.model.PricingMethod

/**
 * Representa os valores digitados no formulário.
 *
 * Os campos numéricos permanecem como String.
 * Aceita vírgula ou ponto como separador decimal.
 *
 * Esta classe não executa cálculos financeiros.
 */
data class CalculatorFormState(

    // Identificação da peça
    val pieceName: String = "",

    // Moeda selecionada para o cálculo
    val currency: CalculationCurrency =
        CalculationCurrency.BRL,

    // Filamento
    val filamentWeightGrams: String = "",
    val filamentPricePerKg: String = "",

    // Tempo de impressão
    val printHours: String = "",
    val printMinutes: String = "0",

    // Impressora selecionada
    val selectedPrinterId: String? = null,
    val averagePowerWatts: String = "",

    // Custos de produção
    val electricityRatePerKwh: String = "",
    val printerWearPerHour: String = "",
    val packagingCost: String = "0",

    // Precificação
    val profitMarginPercent: String = "",

    // Comissão opcional
    val commissionEnabled: Boolean = false,
    val commissionPercent: String = "",

    // Método de formação do preço
    val pricingMethod: PricingMethod =
        PricingMethod.COST_MARKUP
)
