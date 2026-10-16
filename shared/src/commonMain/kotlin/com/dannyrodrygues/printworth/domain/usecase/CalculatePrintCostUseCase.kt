
package com.dannyrodrygues.printworth.domain.usecase

import com.dannyrodrygues.printworth.domain.model.Money
import com.dannyrodrygues.printworth.domain.model.PrintCalculationInput
import com.dannyrodrygues.printworth.domain.model.PrintCalculationResult

/**
 * Calcula custos e preço sugerido de uma impressão 3D.
 *
 * Utiliza aritmética inteira e arredondamento explícito.
 * Não depende do Android nem do Compose.
 */
class CalculatePrintCostUseCase {

    operator fun invoke(
        input: PrintCalculationInput
    ): PrintCalculationResult {

        validate(input)

        val totalMinutes = FixedPointMath.addExactNonNegative(
            input.printHours.toLong() * 60L,
            input.printMinutes.toLong()
        )

        // Filamento: centigramas e preço por quilograma.
        val filamentCost = Money(
            FixedPointMath.multiplyDivideHalfUp(
                input.filamentWeightCentigrams,
                input.filamentPricePerKg.tenThousandthsOfReal,
                10_000_000L
            )
        )

        // Energia: potência média, tempo e tarifa por kWh.
        val powerMinutes = multiplyExactNonNegative(
            input.averagePowerWatts.toLong(),
            totalMinutes
        )

        val energyCost = Money(
            FixedPointMath.multiplyDivideHalfUp(
                powerMinutes,
                input.electricityRatePerKwh.tenThousandthsOfReal,
                6_000_000L
            )
        )

        // Desgaste proporcional à duração.
        val printerWearCost = Money(
            FixedPointMath.multiplyDivideHalfUp(
                totalMinutes,
                input.printerWearPerHour.tenThousandthsOfReal,
                6_000L
            )
        )

        val packagingCost = input.packagingCost

        // Soma segura dos custos finais arredondados.
        val baseCostCents = listOf(
            filamentCost.cents,
            energyCost.cents,
            printerWearCost.cents,
            packagingCost.cents
        ).fold(0L) { total, cost ->
            FixedPointMath.addExactNonNegative(total, cost)
        }

        val baseCost = Money(baseCostCents)

        // Comissão desativada não participa do cálculo.
        val commissionBasisPoints =
            if (input.commissionEnabled) {
                input.commissionBasisPoints
            } else {
                0
            }

        // 10000 pontos-base representam 100%.
        val denominator = (
                10_000 -
                        input.profitMarginBasisPoints -
                        commissionBasisPoints
                ).toLong()

        // Preço arredondado para cima até o centavo.
        val salePriceCents =
            FixedPointMath.multiplyDivideCeiling(
                baseCost.cents,
                10_000L,
                denominator
            )

        val suggestedSalePrice = Money(salePriceCents)

        // Comissão calculada sobre o preço arredondado.
        val commissionCents =
            FixedPointMath.multiplyDivideHalfUp(
                salePriceCents,
                commissionBasisPoints.toLong(),
                10_000L
            )

        val commissionAmount = Money(commissionCents)

        // Confere a integridade da operação.
        val remainingAfterCosts =
            salePriceCents - baseCost.cents

        if (commissionCents > remainingAfterCosts) {
            throw ArithmeticException(
                "Comissão superior ao valor disponível."
            )
        }

        val profitCents =
            remainingAfterCosts - commissionCents

        val estimatedProfit = Money(profitCents)

        // Margem efetiva após os arredondamentos.
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
            effectiveMarginBasisPoints = effectiveMarginBasisPoints
        )
    }

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
            "A margem não pode ser negativa."
        }

        val commission = if (input.commissionEnabled) {
            input.commissionBasisPoints
        } else {
            0
        }

        require(commission >= 0) {
            "A comissão não pode ser negativa."
        }

        require(
            input.profitMarginBasisPoints.toLong() +
                    commission.toLong() < 10_000L
        ) {
            "A soma da margem e comissão deve ser inferior a 100%."
        }
    }

    private fun multiplyExactNonNegative(
        first: Long,
        second: Long
    ): Long {
        require(first >= 0L && second >= 0L)

        if (first != 0L && second > Long.MAX_VALUE / first) {
            throw ArithmeticException(
                "Estouro numérico na multiplicação."
            )
        }

        return first * second
    }
}
