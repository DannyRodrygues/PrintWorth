
package com.dannyrodrygues.printworth.domain.usecase

/**
 * Operações matemáticas de precisão fixa utilizadas
 * nas regras financeiras do PrintWorth.
 *
 * Não utiliza Float ou Double.
 *
 * Aceita operandos não negativos e divisores positivos.
 * Detecta operações que excedam a capacidade de Long.
 */
internal object FixedPointMath {

    /**
     * Multiplica dois valores, divide pelo divisor
     * e arredonda para o inteiro mais próximo.
     *
     * HALF_UP: valores exatamente no meio
     * são arredondados para cima.
     */
    fun multiplyDivideHalfUp(
        first: Long,
        second: Long,
        divisor: Long
    ): Long {
        return multiplyDivide(
            first = first,
            second = second,
            divisor = divisor,
            roundUp = false
        )
    }

    /**
     * Multiplica dois valores, divide pelo divisor
     * e arredonda sempre para cima.
     *
     * Será utilizado no cálculo do preço de venda.
     */
    fun multiplyDivideCeiling(
        first: Long,
        second: Long,
        divisor: Long
    ): Long {
        return multiplyDivide(
            first = first,
            second = second,
            divisor = divisor,
            roundUp = true
        )
    }

    /**
     * Soma dois valores não negativos verificando
     * se o resultado cabe em Long.
     */
    fun addExactNonNegative(
        first: Long,
        second: Long
    ): Long {
        require(first >= 0L && second >= 0L) {
            "Os valores devem ser não negativos."
        }

        if (first > Long.MAX_VALUE - second) {
            throw ArithmeticException(
                "Estouro numérico na soma."
            )
        }

        return first + second
    }

    private fun multiplyDivide(
        first: Long,
        second: Long,
        divisor: Long,
        roundUp: Boolean
    ): Long {

        require(first >= 0L && second >= 0L) {
            "Os valores devem ser não negativos."
        }

        require(divisor > 0L) {
            "O divisor deve ser maior que zero."
        }

        // Simplifica as frações antes da multiplicação
        // para reduzir o risco de estouro numérico.
        val firstGcd = greatestCommonDivisor(
            first,
            divisor
        )

        val reducedFirst = first / firstGcd
        var reducedDivisor = divisor / firstGcd

        val secondGcd = greatestCommonDivisor(
            second,
            reducedDivisor
        )

        val reducedSecond = second / secondGcd
        reducedDivisor /= secondGcd

        // Verificação antes de multiplicar.
        if (
            reducedFirst != 0L &&
            reducedSecond > Long.MAX_VALUE / reducedFirst
        ) {
            throw ArithmeticException(
                "Valores excedem a precisão suportada."
            )
        }

        val numerator = reducedFirst * reducedSecond

        val quotient = numerator / reducedDivisor
        val remainder = numerator % reducedDivisor

        val shouldRoundUp = if (roundUp) {
            remainder > 0L
        } else {
            remainder >= reducedDivisor - remainder
        }

        if (!shouldRoundUp) {
            return quotient
        }

        if (quotient == Long.MAX_VALUE) {
            throw ArithmeticException(
                "Estouro numérico no arredondamento."
            )
        }

        return quotient + 1L
    }

    private fun greatestCommonDivisor(
        first: Long,
        second: Long
    ): Long {
        var a = first
        var b = second

        while (b != 0L) {
            val remainder = a % b
            a = b
            b = remainder
        }

        return a
    }
}
