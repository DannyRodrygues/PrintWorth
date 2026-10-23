
package com.dannyrodrygues.printworth.domain.usecase

import com.dannyrodrygues.printworth.domain.model.Money
import com.dannyrodrygues.printworth.domain.model.PricingMethod
import com.dannyrodrygues.printworth.domain.model.PrintCalculationInput
import com.dannyrodrygues.printworth.domain.model.PrintCalculationResult

/**
 * Calcula os custos de uma impressão 3D e sugere
 * o preço de venda conforme o método selecionado.
 *
 * Métodos:
 * - COST_MARKUP: lucro sobre o custo.
 * - SALE_MARGIN: margem sobre o preço de venda.
 *
 * A comissão opcional incide sobre o preço de venda.
 *
 * Utiliza aritmética inteira, sem Float ou Double.
 */
class CalculatePrintCostUseCase {

    operator fun invoke(
        input: PrintCalculationInput
    ): PrintCalculationResult {

        validate(input)

        // Duração total em minutos.
        val totalMinutes =
            FixedPointMath.addExactNonNegative(
                input.printHours.toLong() * 60L,
                input.printMinutes.toLong()
            )

        // Custo do filamento.
        val filamentCost = Money(
            FixedPointMath.multiplyDivideHalfUp(
                input.filamentWeightCentigrams,
                input.filamentPricePerKg.tenThousandthsOfReal,
                10_000_000L
            )
        )

        // Potência multiplicada pela duração.
        val powerMinutes = multiplyExactNonNegative(
            input.averagePowerWatts.toLong(),
            totalMinutes
        )

        // Custo de energia elétrica.
        val energyCost = Money(
            FixedPointMath.multiplyDivideHalfUp(
                powerMinutes,
                input.electricityRatePerKwh.tenThousandthsOfReal,
                6_000_000L
            )
        )

        // Custo de desgaste da impressora.
        val printerWearCost = Money(
            FixedPointMath.multiplyDivideHalfUp(
                totalMinutes,
                input.printerWearPerHour.tenThousandthsOfReal,
                6_000L
            )
        )

        // Custo opcional da embalagem.
        val packagingCost = input.packagingCost

        // Soma dos componentes arredondados.
        val baseCostCents = listOf(
            filamentCost.cents,
            energyCost.cents,
            printerWearCost.cents,
            packagingCost.cents
        ).fold(0L) { total, cost ->
            FixedPointMath.addExactNonNegative(
                total,
                cost
            )
        }

        val baseCost = Money(baseCostCents)

        // Comissão desativada é sempre zero.
        val commissionBasisPoints =
            if (input.commissionEnabled) {
                input.commissionBasisPoints
            } else {
                0
            }

        /*
         * O nome profitMarginBasisPoints foi mantido
         * temporariamente para compatibilidade.
         *
         * Seu significado depende do método:
         *
         * COST_MARKUP:
         * Percentual de lucro sobre o custo.
         *
         * SALE_MARGIN:
         * Margem desejada sobre o preço de venda.
         */
        val desiredPercentageBasisPoints =
            input.profitMarginBasisPoints.toLong()

        // Denominador para compensar a comissão.
        val commissionDenominator =
            10_000L - commissionBasisPoints.toLong()

        // Calcula o preço segundo o método escolhido.
        val salePriceCents = when (input.pricingMethod) {

            PricingMethod.COST_MARKUP -> {

                /*
                 * Lucro sobre o custo.
                 *
                 * Exemplo sem comissão:
                 * Custo = R$ 20,00
                 * Lucro = 100%
                 * Venda = R$ 40,00
                 */

                val multiplier =
                    FixedPointMath.addExactNonNegative(
                        10_000L,
                        desiredPercentageBasisPoints
                    )

                val minimumNetRevenueCents =
                    FixedPointMath.multiplyDivideCeiling(
                        baseCost.cents,
                        multiplier,
                        10_000L
                    )

                // Compensa a comissão, quando ativada.
                FixedPointMath.multiplyDivideCeiling(
                    minimumNetRevenueCents,
                    10_000L,
                    commissionDenominator
                )
            }

            PricingMethod.SALE_MARGIN -> {

                /*
                 * Margem sobre a venda.
                 *
                 * Preço =
                 * custo / (1 - margem - comissão)
                 *
                 * Exemplo sem comissão:
                 * Custo = R$ 20,00
                 * Margem = 50%
                 * Venda = R$ 40,00
                 */

                val denominator =
                    10_000L -
                            desiredPercentageBasisPoints -
                            commissionBasisPoints.toLong()

                FixedPointMath.multiplyDivideCeiling(
                    baseCost.cents,
                    10_000L,
                    denominator
                )
            }
        }

        val suggestedSalePrice = Money(salePriceCents)

        // Comissão sobre o preço arredondado.
        val commissionCents =
            FixedPointMath.multiplyDivideHalfUp(
                salePriceCents,
                commissionBasisPoints.toLong(),
                10_000L
            )

        val commissionAmount = Money(commissionCents)

        // Valor disponível após subtrair os custos.
        val remainingAfterCosts =
            salePriceCents - baseCost.cents

        if (commissionCents > remainingAfterCosts) {
            throw ArithmeticException(
                "A comissão excede o valor disponível."
            )
        }

        // Lucro estimado após custos e comissão.
        val profitCents =
            remainingAfterCosts - commissionCents

        val estimatedProfit = Money(profitCents)

        /*
         * Margem efetiva sobre o preço de venda.
         * É calculada para ambos os métodos.
         */
        val effectiveMarginBasisPoints =
            if (salePriceCents == 0L) {
                null
            } else {
                FixedPointMath.multiplyDivideHalfUp(
                    profitCents,
                    10_000L,
                    salePriceCents
                ).toInt()
            }

        return PrintCalculationResult(
            pieceName = input.pieceName,
            filamentCost = filamentCost,
            energyCost = energyCost,
            printerWearCost = printerWearCost,
            packagingCost = packagingCost,
            baseCost = baseCost,
            suggestedSalePrice = suggestedSalePrice,
            commissionAmount = commissionAmount,
            estimatedProfit = estimatedProfit,
            effectiveMarginBasisPoints =
                effectiveMarginBasisPoints
        )
    }

    /**
     * Validação das regras de domínio.
     */
    private fun validate(input: PrintCalculationInput) {

        require(input.filamentWeightCentigrams > 0L) {
            "O peso do filamento deve ser maior que zero."
        }

        require(
            input.filamentPricePerKg.tenThousandthsOfReal >= 0L
        ) {
            "O preço do filamento não pode ser negativo."
        }

        require(
            input.printHours >= 0 &&
                    input.printMinutes in 0..59 &&
                    (input.printHours > 0 || input.printMinutes > 0)
        ) {
            "Informe uma duração válida."
        }

        require(
            input.printer.id.isNotBlank() &&
                    input.printer.name.isNotBlank()
        ) {
            "Selecione uma impressora válida."
        }

        require(input.printer.averagePowerWatts > 0) {
            "A potência cadastrada deve ser positiva."
        }

        require(input.averagePowerWatts > 0) {
            "A potência utilizada deve ser maior que zero."
        }

        require(
            input.electricityRatePerKwh.tenThousandthsOfReal >= 0L
        ) {
            "A tarifa de energia não pode ser negativa."
        }

        require(
            input.printerWearPerHour.tenThousandthsOfReal >= 0L
        ) {
            "O desgaste não pode ser negativo."
        }

        require(input.packagingCost.cents >= 0L) {
            "O custo de embalagem não pode ser negativo."
        }

        require(input.profitMarginBasisPoints >= 0) {
            "O percentual desejado não pode ser negativo."
        }

        val commission =
            if (input.commissionEnabled) {
                input.commissionBasisPoints
            } else {
                0
            }

        require(commission in 0 until 10_000) {
            "A comissão deve ser menor que 100%."
        }

        /*
         * Somente o método SALE_MARGIN exige
         * que margem + comissão seja menor que 100%.
         *
         * COST_MARKUP aceita lucros de 100% ou mais.
         */
        if (input.pricingMethod == PricingMethod.SALE_MARGIN) {

            require(
                input.profitMarginBasisPoints.toLong() +
                        commission.toLong() < 10_000L
            ) {
                "A soma da margem sobre a venda e da " +
                        "comissão deve ser inferior a 100%."
            }
        }
    }

    /**
     * Multiplica dois valores não negativos,
     * detectando estouro de Long.
     */
    private fun multiplyExactNonNegative(
        first: Long,
        second: Long
    ): Long {

        require(first >= 0L && second >= 0L) {
            "Os valores devem ser não negativos."
        }

        if (
            first != 0L &&
            second > Long.MAX_VALUE / first
        ) {
            throw ArithmeticException(
                "Estouro numérico na multiplicação."
            )
        }

        return first * second
    }
}
