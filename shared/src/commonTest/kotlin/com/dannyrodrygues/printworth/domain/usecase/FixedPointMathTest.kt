
package com.dannyrodrygues.printworth.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FixedPointMathTest {

    @Test
    fun halfUpRoundsHalfValuesUp() {
        val result = FixedPointMath.multiplyDivideHalfUp(
            first = 125L,
            second = 1L,
            divisor = 10L
        )

        assertEquals(13L, result)
    }

    @Test
    fun halfUpRoundsValuesBelowHalfDown() {
        val result = FixedPointMath.multiplyDivideHalfUp(
            first = 124L,
            second = 1L,
            divisor = 10L
        )

        assertEquals(12L, result)
    }

    @Test
    fun ceilingAlwaysRoundsPositiveRemainderUp() {
        val result = FixedPointMath.multiplyDivideCeiling(
            first = 121L,
            second = 1L,
            divisor = 10L
        )

        assertEquals(13L, result)
    }

    @Test
    fun zeroRemainsZero() {
        val result = FixedPointMath.multiplyDivideCeiling(
            first = 0L,
            second = 100L,
            divisor = 7L
        )

        assertEquals(0L, result)
    }

    @Test
    fun additionDetectsOverflow() {
        assertFailsWith<ArithmeticException> {
            FixedPointMath.addExactNonNegative(
                Long.MAX_VALUE,
                1L
            )
        }
    }

    @Test
    fun multiplicationDetectsOverflow() {
        assertFailsWith<ArithmeticException> {
            FixedPointMath.multiplyDivideHalfUp(
                Long.MAX_VALUE,
                Long.MAX_VALUE,
                2L
            )
        }
    }

    @Test
    fun fractionReductionAvoidsUnnecessaryOverflow() {
        val result = FixedPointMath.multiplyDivideHalfUp(
            Long.MAX_VALUE,
            Long.MAX_VALUE,
            Long.MAX_VALUE
        )

        assertEquals(Long.MAX_VALUE, result)
    }

    @Test
    fun divisionByZeroIsRejected() {
        assertFailsWith<IllegalArgumentException> {
            FixedPointMath.multiplyDivideHalfUp(
                100L,
                50L,
                0L
            )
        }
    }
}
