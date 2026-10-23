
package com.dannyrodrygues.printworth.presentation.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DecimalInputParserTest {

    @Test
    fun convertsWeightWithComma() {
        val result = DecimalInputParser.parse(
            rawValue = "125,50",
            fractionDigits = 2,
            fieldName = "Peso"
        )

        val success =
            assertIs<DecimalParseResult.Success>(result)

        assertEquals(12_550L, success.scaledValue)
    }

    @Test
    fun convertsMoneyRateWithFourDecimalPlaces() {
        val result = DecimalInputParser.parse(
            rawValue = "100,00",
            fractionDigits = 4,
            fieldName = "Preço"
        )

        val success =
            assertIs<DecimalParseResult.Success>(result)

        assertEquals(1_000_000L, success.scaledValue)
    }

    @Test
    fun convertsPercentageToBasisPoints() {
        val result = DecimalInputParser.parse(
            rawValue = "20,5",
            fractionDigits = 2,
            fieldName = "Margem"
        )

        val success =
            assertIs<DecimalParseResult.Success>(result)

        assertEquals(2_050L, success.scaledValue)
    }

    @Test
    fun acceptsDecimalPoint() {
        val result = DecimalInputParser.parse(
            rawValue = "12.75",
            fractionDigits = 2,
            fieldName = "Valor"
        )

        val success =
            assertIs<DecimalParseResult.Success>(result)

        assertEquals(1_275L, success.scaledValue)
    }

    @Test
    fun rejectsNegativeValues() {
        val result = DecimalInputParser.parse(
            rawValue = "-10,50",
            fractionDigits = 2,
            fieldName = "Valor"
        )

        assertIs<DecimalParseResult.Error>(result)
    }

    @Test
    fun rejectsTooManyDecimalPlaces() {
        val result = DecimalInputParser.parse(
            rawValue = "10,12345",
            fractionDigits = 4,
            fieldName = "Tarifa"
        )

        assertIs<DecimalParseResult.Error>(result)
    }

    @Test
    fun rejectsThousandsSeparators() {
        val result = DecimalInputParser.parse(
            rawValue = "1.234,50",
            fractionDigits = 2,
            fieldName = "Valor"
        )

        assertIs<DecimalParseResult.Error>(result)
    }

    @Test
    fun rejectsNumericOverflow() {
        val result = DecimalInputParser.parse(
            rawValue = "999999999999999999999",
            fractionDigits = 2,
            fieldName = "Valor"
        )

        assertIs<DecimalParseResult.Error>(result)
    }

    @Test
    fun acceptsZero() {
        val result = DecimalInputParser.parse(
            rawValue = "0,00",
            fractionDigits = 2,
            fieldName = "Valor"
        )

        val success =
            assertIs<DecimalParseResult.Success>(result)

        assertEquals(0L, success.scaledValue)
    }
}
