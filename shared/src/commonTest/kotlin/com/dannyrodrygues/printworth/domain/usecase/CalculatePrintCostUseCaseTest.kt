
package com.dannyrodrygues.printworth.domain.usecase

import com.dannyrodrygues.printworth.domain.model.Money
import com.dannyrodrygues.printworth.domain.model.MoneyRate
import com.dannyrodrygues.printworth.domain.model.Printer
import com.dannyrodrygues.printworth.domain.model.PrintCalculationInput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CalculatePrintCostUseCaseTest {

    private val useCase = CalculatePrintCostUseCase()

    private fun sampleInput() = PrintCalculationInput(
        pieceName = "Peça de teste",
        filamentWeightCentigrams = 10_000L,
        filamentPricePerKg = MoneyRate(1_000_000L),
        printHours = 5,
        printMinutes = 0,
        printer = Printer(
            id = "test-printer",
            name = "Impressora de teste",
            averagePowerWatts = 150
        ),
        averagePowerWatts = 150,
        electricityRatePerKwh = MoneyRate(10_000L),
        printerWearPerHour = MoneyRate(10_000L),
        packagingCost = Money(200L),
        profitMarginBasisPoints = 2_000,
        commissionEnabled = true,
        commissionBasisPoints = 1_000
    )

    @Test
    fun referenceExampleProducesExpectedValues() {
        val result = useCase(sampleInput())

        assertEquals(1_000L, result.filamentCost.cents)
        assertEquals(75L, result.energyCost.cents)
        assertEquals(500L, result.printerWearCost.cents)
        assertEquals(200L, result.packagingCost.cents)
        assertEquals(1_775L, result.baseCost.cents)
        assertEquals(2_536L, result.suggestedSalePrice.cents)
        assertEquals(254L, result.commissionAmount.cents)
        assertEquals(507L, result.estimatedProfit.cents)
        assertEquals(1_999, result.effectiveMarginBasisPoints)
    }

    @Test
    fun priceEqualsCostsCommissionAndProfit() {
        val result = useCase(sampleInput())

        assertEquals(
            result.suggestedSalePrice.cents,
            result.baseCost.cents +
                    result.commissionAmount.cents +
                    result.estimatedProfit.cents
        )
    }

    @Test
    fun disabledCommissionIsIgnored() {
        val input = sampleInput().copy(
            commissionEnabled = false
        )

        val result = useCase(input)

        assertEquals(0L, result.commissionAmount.cents)
        assertEquals(2_219L, result.suggestedSalePrice.cents)
    }

    @Test
    fun rejectsMarginAndCommissionAtOneHundredPercent() {
        val input = sampleInput().copy(
            profitMarginBasisPoints = 9_000,
            commissionBasisPoints = 1_000
        )

        assertFailsWith<IllegalArgumentException> {
            useCase(input)
        }
    }

    @Test
    fun rejectsZeroDuration() {
        val input = sampleInput().copy(
            printHours = 0,
            printMinutes = 0
        )

        assertFailsWith<IllegalArgumentException> {
            useCase(input)
        }
    }

    @Test
    fun zeroCostProducesUndefinedEffectiveMargin() {
        val input = sampleInput().copy(
            filamentPricePerKg = MoneyRate(0L),
            electricityRatePerKwh = MoneyRate(0L),
            printerWearPerHour = MoneyRate(0L),
            packagingCost = Money(0L)
        )

        val result = useCase(input)

        assertEquals(0L, result.suggestedSalePrice.cents)
        assertEquals(0L, result.estimatedProfit.cents)
        assertNull(result.effectiveMarginBasisPoints)
    }


    @Test
    fun calculatesFractionalPrintDurationCorrectly() {
        val input = sampleInput().copy(
            printHours = 2,
            printMinutes = 30
        )

        val result = useCase(input)

        assertEquals(38L, result.energyCost.cents)
        assertEquals(250L, result.printerWearCost.cents)
        assertEquals(1488L, result.baseCost.cents)
        assertEquals(2126L, result.suggestedSalePrice.cents)
        assertEquals(213L, result.commissionAmount.cents)
        assertEquals(425L, result.estimatedProfit.cents)
    }

    @Test
    fun roundsHalfCentUp() {
        val input = sampleInput().copy(
            filamentWeightCentigrams = 5L
        )

        val result = useCase(input)

        assertEquals(1L, result.filamentCost.cents)
    }

    @Test
    fun zeroMarginWithoutCommissionProducesNoProfit() {
        val input = sampleInput().copy(
            profitMarginBasisPoints = 0,
            commissionEnabled = false
        )

        val result = useCase(input)

        assertEquals(
            result.baseCost.cents,
            result.suggestedSalePrice.cents
        )
        assertEquals(0L, result.commissionAmount.cents)
        assertEquals(0L, result.estimatedProfit.cents)
    }

    @Test
    fun rejectsNegativePackagingCost() {
        val input = sampleInput().copy(
            packagingCost = Money(-100L)
        )

        assertFailsWith<IllegalArgumentException> {
            useCase(input)
        }
    }

}
