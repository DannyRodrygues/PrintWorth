
package com.dannyrodrygues.printworth.domain.model

/**
 * Representa o resultado do cálculo de custo
 * e precificação de uma impressão 3D.
 *
 * Todos os valores monetários são armazenados
 * em centavos através do tipo Money.
 *
 * Esta classe contém somente dados.
 * As fórmulas pertencem à camada domain.usecase.
 */
data class PrintCalculationResult(

    // Identificação opcional da peça
    val pieceName: String?,

    // Custos individuais
    val filamentCost: Money,
    val energyCost: Money,
    val printerWearCost: Money,
    val packagingCost: Money,

    // Soma dos custos de produção
    val baseCost: Money,

    // Preço final sugerido
    val suggestedSalePrice: Money,

    // Comissão calculada sobre o preço de venda
    val commissionAmount: Money,

    // Lucro líquido estimado após os custos e a comissão
    val estimatedProfit: Money,

    /**
     * Margem efetiva após os arredondamentos.
     *
     * Representada em pontos-base:
     * 2000 = 20,00%
     * 2055 = 20,55%
     *
     * Null quando o preço de venda é zero,
     * evitando divisão por zero.
     */
    val effectiveMarginBasisPoints: Int?
)
