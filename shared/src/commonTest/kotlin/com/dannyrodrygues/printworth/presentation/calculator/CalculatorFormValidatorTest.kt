
package com.dannyrodrygues.printworth.presentation.calculator

import com.dannyrodrygues.printworth.domain.model.Printer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import com.dannyrodrygues.printworth.domain.model.PricingMethod

class CalculatorFormValidatorTest {

    private val validator = CalculatorFormValidator()

    private val printer = Printer(
        id = "printer-1",
        name = "Impressora de teste",
        averagePowerWatts = 150
    )

    private fun validState() = CalculatorFormState(
        pieceName = "Peça de teste",
        filamentWeightGrams = "125,50",
        filamentPricePerKg = "100,00",
        printHours = "2",
        printMinutes = "30",
        selectedPrinterId = "printer-1",
        averagePowerWatts = "150",
        electricityRatePerKwh = "1,0000",
        printerWearPerHour = "1,2500",
        packagingCost = "2,50",
        profitMarginPercent = "20,50",
        commissionEnabled = true,
        commissionPercent = "10,00"
    )

    @Test
    fun validFormProducesCorrectDomainInput() {
        val result = validator.validate(
            validState(),
            printer
        )

        val success =
            assertIs<CalculatorFormValidationResult.Success>(
                result
            )

        val input = success.input

        assertEquals(
            12_550L,
            input.filamentWeightCentigrams
        )
        assertEquals(
            1_000_000L,
            input.filamentPricePerKg.tenThousandthsOfReal
        )
        assertEquals(2, input.printHours)
        assertEquals(30, input.printMinutes)
        assertEquals(150, input.averagePowerWatts)
        assertEquals(250L, input.packagingCost.cents)
        assertEquals(2050, input.profitMarginBasisPoints)
        assertEquals(1000, input.commissionBasisPoints)
    }

    @Test
    fun rejectsZeroFilamentWeight() {
        val state = validState().copy(
            filamentWeightGrams = "0"
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.FILAMENT_WEIGHT]
        )
    }

    @Test
    fun rejectsZeroPrintDuration() {
        val state = validState().copy(
            printHours = "0",
            printMinutes = "0"
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.PRINT_HOURS]
        )
    }

    @Test
    fun rejectsMinutesAbove59() {
        val state = validState().copy(
            printMinutes = "75"
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.PRINT_MINUTES]
        )
    }

    @Test
    fun rejectsMissingPrinter() {
        val state = validState().copy(
            selectedPrinterId = null
        )

        val result = validator.validate(state, null)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.PRINTER]
        )
    }

    @Test
    fun rejectsDifferentPrinterSelection() {
        val state = validState().copy(
            selectedPrinterId = "printer-2"
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.PRINTER]
        )
    }

    @Test
    fun acceptsOneHundredPercentProfitWithCommission() {
        val state = validState().copy(
            profitMarginPercent = "100",
            commissionPercent = "10"
        )

        val result = validator.validate(state, printer)

        val success =
            assertIs<CalculatorFormValidationResult.Success>(result)

        assertEquals(
            10_000,
            success.input.profitMarginBasisPoints
        )

        assertEquals(
            1_000,
            success.input.commissionBasisPoints
        )
    }

    @Test
    fun acceptsTwoHundredPercentProfit() {
        val state = validState().copy(
            profitMarginPercent = "200",
            commissionEnabled = false
        )

        val result = validator.validate(state, printer)

        val success =
            assertIs<CalculatorFormValidationResult.Success>(result)

        assertEquals(
            20_000,
            success.input.profitMarginBasisPoints
        )
    }

    @Test
    fun rejectsCommissionAtOneHundredPercent() {
        val state = validState().copy(
            profitMarginPercent = "100",
            commissionPercent = "100"
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(result)

        assertNotNull(
            failure.errors[CalculatorFormField.COMMISSION]
        )
    }


    @Test
    fun disabledCommissionIsIgnored() {
        val state = validState().copy(
            commissionEnabled = false,
            commissionPercent = "valor inválido"
        )

        val result = validator.validate(state, printer)

        val success =
            assertIs<CalculatorFormValidationResult.Success>(
                result
            )

        assertEquals(
            0,
            success.input.commissionBasisPoints
        )

        assertTrue(
            !success.input.commissionEnabled
        )
    }

    @Test
    fun emptyPackagingBecomesZero() {
        val state = validState().copy(
            packagingCost = ""
        )

        val result = validator.validate(state, printer)

        val success =
            assertIs<CalculatorFormValidationResult.Success>(
                result
            )

        assertEquals(
            0L,
            success.input.packagingCost.cents
        )
    }

    @Test
    fun rejectsWeightWithExcessDecimalPlaces() {
        val state = validState().copy(
            filamentWeightGrams = "125,123"
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.FILAMENT_WEIGHT]
        )
    }

    @Test
    fun rejectsNegativeEnergyRate() {
        val state = validState().copy(
            electricityRatePerKwh = "-1,50"
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.ELECTRICITY_RATE]
        )
    }


    @Test
    fun saleMarginAcceptsFiftyPercent() {
        val state = validState().copy(
            pricingMethod = PricingMethod.SALE_MARGIN,
            profitMarginPercent = "50",
            commissionEnabled = false
        )

        val result = validator.validate(state, printer)

        val success =
            assertIs<CalculatorFormValidationResult.Success>(result)

        assertEquals(
            PricingMethod.SALE_MARGIN,
            success.input.pricingMethod
        )

        assertEquals(
            5_000,
            success.input.profitMarginBasisPoints
        )
    }

    @Test
    fun saleMarginRejectsOneHundredPercent() {
        val state = validState().copy(
            pricingMethod = PricingMethod.SALE_MARGIN,
            profitMarginPercent = "100",
            commissionEnabled = false
        )

        val result = validator.validate(state, printer)

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(result)

        assertNotNull(
            failure.errors[CalculatorFormField.PROFIT_MARGIN]
        )
    }


    @Test
    fun catalogPrinterWithoutSuggestedPowerUsesEnteredPower() {
        val catalogPrinter = Printer(
            id = "creality-ender-3-v3-se",
            name = "Creality Ender-3 V3 SE",
            averagePowerWatts = 0
        )

        val state = validState().copy(
            selectedPrinterId = catalogPrinter.id,
            averagePowerWatts = "150"
        )

        val result = validator.validate(
            state,
            catalogPrinter
        )

        val success =
            assertIs<CalculatorFormValidationResult.Success>(
                result
            )

        assertEquals(
            150,
            success.input.printer.averagePowerWatts
        )
    }

    @Test
    fun catalogPrinterWithoutPowerRequiresUserInput() {
        val catalogPrinter = Printer(
            id = "creality-ender-3-v3-se",
            name = "Creality Ender-3 V3 SE",
            averagePowerWatts = 0
        )

        val state = validState().copy(
            selectedPrinterId = catalogPrinter.id,
            averagePowerWatts = ""
        )

        val result = validator.validate(
            state,
            catalogPrinter
        )

        val failure =
            assertIs<CalculatorFormValidationResult.Failure>(
                result
            )

        assertNotNull(
            failure.errors[CalculatorFormField.AVERAGE_POWER]
        )
    }


}
