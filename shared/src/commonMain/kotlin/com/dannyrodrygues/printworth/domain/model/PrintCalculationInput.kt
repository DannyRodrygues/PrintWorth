
package com.dannyrodrygues.printworth.domain.model

/**
 * Representa os dados necessários para
 * calcular o custo de uma impressão 3D.
 *
 * Valores monetários utilizam Money ou MoneyRate.
 *
 * O peso é representado em centigramas:
 * 1 grama = 100 centigramas.
 *
 * Os percentuais utilizam pontos-base:
 * 100 pontos-base = 1%.
 * 2000 pontos-base = 20%.
 */
data class PrintCalculationInput(

    // Identificação da peça
    val pieceName: String? = null,

    // Consumo de filamento
    val filamentWeightCentigrams: Long,
    val filamentPricePerKg: MoneyRate,

    // Tempo de impressão
    val printHours: Int,
    val printMinutes: Int,

    // Impressora selecionada
    val printer: Printer,

    // Potência utilizada neste cálculo
    val averagePowerWatts: Int,

    // Energia elétrica
    val electricityRatePerKwh: MoneyRate,

    // Desgaste e manutenção
    val printerWearPerHour: MoneyRate,

    // Embalagem opcional
    val packagingCost: Money = Money(0L),

    // Margem de lucro desejada
    val profitMarginBasisPoints: Int,

    // Comissão opcional
    val commissionEnabled: Boolean = false,
    val commissionBasisPoints: Int = 0,

    // Método de formação do preço
    val pricingMethod: PricingMethod = PricingMethod.COST_MARKUP
)
