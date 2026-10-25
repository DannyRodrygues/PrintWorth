
package com.dannyrodrygues.printworth.presentation.format

import com.dannyrodrygues.printworth.domain.model.CalculationCurrency

/**
 * Responsável pela formatação de valores monetários
 * nas três moedas disponíveis no PrintWorth.
 *
 * Moedas:
 * - BRL: Real brasileiro
 * - USD: Dólar americano
 * - EUR: Euro
 *
 * Os valores são recebidos em centavos.
 *
 * Não realiza conversão cambial e não utiliza
 * Float ou Double.
 */
object MoneyFormatter {

    fun format(
        amountInCents: Long,
        currency: CalculationCurrency
    ): String {

        val negative = amountInCents < 0L

        // Extrai a parte inteira sem perder
        // a precisão monetária.
        val wholePart = (amountInCents / 100L)
            .toString()
            .removePrefix("-")

        // Extrai os centavos.
        val fraction = amountInCents % 100L

        val fractionDigits = (
                if (fraction < 0L) -fraction else fraction
                ).toString().padStart(2, '0')

        // Adiciona o separador de milhares
        // conforme a moeda selecionada.
        val groupedWholePart = wholePart
            .reversed()
            .chunked(3)
            .joinToString(
                currency.groupingSeparator.toString()
            )
            .reversed()

        val sign = if (negative) "-" else ""

        return buildString {
            append(sign)
            append(currency.symbol)
            append(" ")
            append(groupedWholePart)
            append(currency.decimalSeparator)
            append(fractionDigits)
        }
    }
}
