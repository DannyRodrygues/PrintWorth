
package com.dannyrodrygues.printworth.presentation.format

import com.dannyrodrygues.printworth.domain.model.CalculationCurrency
import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyFormatterTest {

    @Test
    fun formatBrazilianReal() {

        val result = MoneyFormatter.format(
            amountInCents = 123456L,
            currency = CalculationCurrency.BRL
        )

        assertEquals("R$ 1.234,56", result)
    }

    @Test
    fun formatAmericanDollar() {

        val result = MoneyFormatter.format(
            amountInCents = 123456L,
            currency = CalculationCurrency.USD
        )

        assertEquals("US$ 1,234.56", result)
    }

    @Test
    fun formatEuro() {

        val result = MoneyFormatter.format(
            amountInCents = 123456L,
            currency = CalculationCurrency.EUR
        )

        assertEquals("€ 1.234,56", result)
    }

    @Test
    fun formatZero() {

        assertEquals(
            "R$ 0,00",
            MoneyFormatter.format(
                0L,
                CalculationCurrency.BRL
            )
        )

        assertEquals(
            "US$ 0.00",
            MoneyFormatter.format(
                0L,
                CalculationCurrency.USD
            )
        )

        assertEquals(
            "€ 0,00",
            MoneyFormatter.format(
                0L,
                CalculationCurrency.EUR
            )
        )
    }

    @Test
    fun formatNegativeValue() {

        assertEquals(
            "-€ 0,01",
            MoneyFormatter.format(
                -1L,
                CalculationCurrency.EUR
            )
        )
    }

    @Test
    fun formatLargeValues() {

        assertEquals(
            "R$ 1.234.567,89",
            MoneyFormatter.format(
                123456789L,
                CalculationCurrency.BRL
            )
        )

        assertEquals(
            "US$ 1,234,567.89",
            MoneyFormatter.format(
                123456789L,
                CalculationCurrency.USD
            )
        )
    }
}
