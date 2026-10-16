
package com.dannyrodrygues.printworth.domain.model

/**
 * Representa um valor monetário final em centavos.
 *
 * Exemplos:
 * R$ 10,00 = 1000 centavos
 * R$ 25,36 = 2536 centavos
 *
 * Não utiliza Float ou Double.
 */
@JvmInline
value class Money(
    val cents: Long
)
