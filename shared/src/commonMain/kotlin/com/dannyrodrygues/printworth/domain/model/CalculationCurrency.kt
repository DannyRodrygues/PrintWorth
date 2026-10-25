
package com.dannyrodrygues.printworth.domain.model

/**
 * Moedas disponíveis no PrintWorth.
 *
 * O usuário informa todos os custos diretamente
 * na moeda escolhida.
 *
 * Não existe conversão cambial.
 */
enum class CalculationCurrency(
    val displayName: String,
    val symbol: String,
    val groupingSeparator: Char,
    val decimalSeparator: Char
) {

    BRL(
        displayName = "Real brasileiro (BRL)",
        symbol = "R$",
        groupingSeparator = '.',
        decimalSeparator = ','
    ),

    USD(
        displayName = "Dólar americano (USD)",
        symbol = "US$",
        groupingSeparator = ',',
        decimalSeparator = '.'
    ),

    EUR(
        displayName = "Euro (EUR)",
        symbol = "€",
        groupingSeparator = '.',
        decimalSeparator = ','
    );

    val code: String
        get() = name
}
