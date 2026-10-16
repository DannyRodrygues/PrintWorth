
package com.dannyrodrygues.printworth.domain.model

/**
 * Representa um valor monetário unitário com
 * quatro casas decimais em reais.
 *
 * Exemplos:
 * R$ 1,0000 = 10000 unidades
 * R$ 1,2345 = 12345 unidades
 *
 * Permite representar tarifas com frações de centavo.
 */
@JvmInline
value class MoneyRate(
    val tenThousandthsOfReal: Long
)
