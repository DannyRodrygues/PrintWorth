
package com.dannyrodrygues.printworth.presentation.calculator

/**
 * Resultado da conversão de um valor decimal.
 */
internal sealed interface DecimalParseResult {

    data class Success(
        val scaledValue: Long
    ) : DecimalParseResult

    data class Error(
        val message: String
    ) : DecimalParseResult
}

/**
 * Converte entradas decimais em inteiros com escala fixa.
 *
 * Exemplos:
 * 125,50 com 2 casas -> 12550
 * 100,00 com 4 casas -> 1000000
 * 20,5 com 2 casas -> 2050
 *
 * Aceita vírgula ou ponto como separador decimal.
 * Não aceita separadores de milhares.
 *
 * Não utiliza Float ou Double.
 */
internal object DecimalInputParser {

    fun parse(
        rawValue: String,
        fractionDigits: Int,
        fieldName: String
    ): DecimalParseResult {

        require(fractionDigits in 0..4) {
            "A escala deve estar entre 0 e 4."
        }

        val value = rawValue.trim()

        if (value.isEmpty()) {
            return DecimalParseResult.Error(
                "$fieldName é obrigatório."
            )
        }

        if (value.startsWith("-")) {
            return DecimalParseResult.Error(
                "$fieldName não pode ser negativo."
            )
        }

        val separatorCount = value.count {
            it == ',' || it == '.'
        }

        if (separatorCount > 1) {
            return DecimalParseResult.Error(
                "$fieldName possui formato inválido."
            )
        }

        val separatorIndex = value.indexOfFirst {
            it == ',' || it == '.'
        }

        val integerPart = if (separatorIndex >= 0) {
            value.substring(0, separatorIndex)
        } else {
            value
        }

        val decimalPart = if (separatorIndex >= 0) {
            value.substring(separatorIndex + 1)
        } else {
            ""
        }

        if (
            !integerPart.hasOnlyAsciiDigits() ||
            (separatorIndex >= 0 &&
                    !decimalPart.hasOnlyAsciiDigits())
        ) {
            return DecimalParseResult.Error(
                "$fieldName deve conter apenas números."
            )
        }

        if (decimalPart.length > fractionDigits) {
            return DecimalParseResult.Error(
                "$fieldName aceita no máximo " +
                        "$fractionDigits casas decimais."
            )
        }

        val scale = when (fractionDigits) {
            0 -> 1L
            1 -> 10L
            2 -> 100L
            3 -> 1_000L
            else -> 10_000L
        }

        val integerValue = integerPart.toLongOrNull()
            ?: return DecimalParseResult.Error(
                "$fieldName excede o limite permitido."
            )

        val fractionalValue = if (decimalPart.isEmpty()) {
            0L
        } else {
            decimalPart
                .padEnd(fractionDigits, '0')
                .toLongOrNull()
                ?: return DecimalParseResult.Error(
                    "$fieldName possui formato inválido."
                )
        }

        if (
            integerValue >
            (Long.MAX_VALUE - fractionalValue) / scale
        ) {
            return DecimalParseResult.Error(
                "$fieldName excede o limite permitido."
            )
        }

        return DecimalParseResult.Success(
            scaledValue = integerValue * scale +
                    fractionalValue
        )
    }

    private fun String.hasOnlyAsciiDigits(): Boolean {
        return isNotEmpty() && all {
            it in '0'..'9'
        }
    }
}
