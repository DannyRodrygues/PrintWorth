
package com.dannyrodrygues.printworth.presentation.calculator

import com.dannyrodrygues.printworth.domain.model.PricingMethod

/**
 * Representa os valores digitados no formulário.
 *
 * Os campos numéricos permanecem como String
 * para permitir entradas brasileiras com vírgula.
 *
 * A conversão para os modelos de domínio será
 * realizada após a validação das entradas.
 *
 * Esta classe não contém regras de cálculo.
 */
data class CalculatorFormState(

    // Identificação da peça
    val pieceName: String = "",

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

    // Método de precificação escolhido.
    val pricingMethod: PricingMethod = PricingMethod.COST_MARKUP
)
