
package com.dannyrodrygues.printworth.presentation.calculator

import com.dannyrodrygues.printworth.domain.model.Printer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CalculatorViewModelTest {

    private val printer = Printer(
        id = "printer-1",
        name = "Impressora de teste",
        averagePowerWatts = 150
    )

    private fun createValidViewModel(): CalculatorViewModel {
        return CalculatorViewModel().apply {
            onPrinterSelected(printer)
            onPieceNameChanged("Peça de teste")
            onFilamentWeightChanged("100")
            onFilamentPriceChanged("100")
            onPrintHoursChanged("5")
            onPrintMinutesChanged("0")
            onElectricityRateChanged("1")
            onPrinterWearChanged("1")
            onPackagingCostChanged("2")
            onProfitMarginChanged("20")
            onCommissionEnabledChanged(true)
            onCommissionPercentChanged("10")
        }
    }

    @Test
    fun validFormCalculatesExpectedResult() {
        val viewModel = createValidViewModel()

        val success = viewModel.calculate()

        assertTrue(success)
        assertTrue(viewModel.fieldErrors.value.isEmpty())
        assertNull(viewModel.generalError.value)

        val result = assertNotNull(
            viewModel.calculationResult.value
        )


        assertEquals(1775L, result.baseCost.cents)
        assertEquals(
            2367L,
            result.suggestedSalePrice.cents
        )
        assertEquals(237L, result.commissionAmount.cents)
        assertEquals(355L, result.estimatedProfit.cents)

    }

    @Test
    fun invalidFormDoesNotCalculate() {
        val viewModel = CalculatorViewModel()

        val success = viewModel.calculate()

        assertFalse(success)
        assertNull(viewModel.calculationResult.value)
        assertTrue(viewModel.fieldErrors.value.isNotEmpty())
    }

    @Test
    fun editingFieldRemovesItsPreviousError() {
        val viewModel = CalculatorViewModel()

        viewModel.calculate()

        assertNotNull(
            viewModel.fieldErrors.value[
                CalculatorFormField.FILAMENT_WEIGHT
            ]
        )

        viewModel.onFilamentWeightChanged("150")

        assertNull(
            viewModel.fieldErrors.value[
                CalculatorFormField.FILAMENT_WEIGHT
            ]
        )
    }

    @Test
    fun changingDataInvalidatesPreviousResult() {
        val viewModel = createValidViewModel()

        assertTrue(viewModel.calculate())
        assertNotNull(viewModel.calculationResult.value)

        viewModel.onFilamentWeightChanged("200")

        assertNull(viewModel.calculationResult.value)

        assertTrue(viewModel.calculate())

        val newResult = assertNotNull(
            viewModel.calculationResult.value
        )

        assertEquals(2775L, newResult.baseCost.cents)
    }

    @Test
    fun selectingPrinterAutomaticallyFillsPower() {
        val viewModel = CalculatorViewModel()

        viewModel.onPrinterSelected(printer)

        assertEquals(
            "printer-1",
            viewModel.formState.value.selectedPrinterId
        )

        assertEquals(
            "150",
            viewModel.formState.value.averagePowerWatts
        )
    }

    @Test
    fun disabledCommissionIsIgnored() {
        val viewModel = createValidViewModel()

        viewModel.onCommissionEnabledChanged(false)
        viewModel.onCommissionPercentChanged("inválido")

        assertTrue(viewModel.calculate())

        val result = assertNotNull(
            viewModel.calculationResult.value
        )


        assertEquals(0L, result.commissionAmount.cents)
        assertEquals(
            2130L,
            result.suggestedSalePrice.cents
        )

    }

    @Test
    fun commissionAtOneHundredPercentPreventsCalculation() {
        val viewModel = createValidViewModel()

        viewModel.onProfitMarginChanged("100")
        viewModel.onCommissionPercentChanged("100")

        assertFalse(viewModel.calculate())
        assertNull(viewModel.calculationResult.value)

        assertNotNull(
            viewModel.fieldErrors.value[
                CalculatorFormField.COMMISSION
            ]
        )
    }

    @Test
    fun oneHundredPercentProfitWithCommissionCalculates() {
        val viewModel = createValidViewModel()

        viewModel.onProfitMarginChanged("100")
        viewModel.onCommissionPercentChanged("10")

        assertTrue(viewModel.calculate())

        val result = assertNotNull(
            viewModel.calculationResult.value
        )

        assertEquals(1775L, result.baseCost.cents)
        assertEquals(3945L, result.suggestedSalePrice.cents)
        assertEquals(395L, result.commissionAmount.cents)
        assertEquals(1775L, result.estimatedProfit.cents)
    }


    @Test
    fun resetFormClearsAllStates() {
        val viewModel = createValidViewModel()

        assertTrue(viewModel.calculate())

        viewModel.resetForm()

        assertEquals(
            CalculatorFormState(),
            viewModel.formState.value
        )

        assertTrue(viewModel.fieldErrors.value.isEmpty())
        assertNull(viewModel.calculationResult.value)
        assertNull(viewModel.generalError.value)
    }
}
