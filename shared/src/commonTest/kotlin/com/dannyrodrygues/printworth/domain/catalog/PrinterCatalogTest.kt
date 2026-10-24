
package com.dannyrodrygues.printworth.domain.catalog

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PrinterCatalogTest {

    @Test
    fun catalogContains26Models() {
        assertEquals(
            26,
            PrinterCatalog.models.size
        )
    }

    @Test
    fun catalogContainsBambuLabA1() {
        val printer = assertNotNull(
            PrinterCatalog.findById("bambu-a1")
        )

        assertEquals("Bambu Lab", printer.brand)
        assertEquals("A1", printer.model)
        assertEquals(
            95,
            printer.suggestedAveragePowerWatts
        )
    }

    @Test
    fun catalogIdsAreUnique() {
        val ids = PrinterCatalog.models.map {
            it.id
        }

        assertEquals(
            ids.size,
            ids.distinct().size
        )
    }

    @Test
    fun catalogContainsAllExpectedBrands() {
        assertEquals(
            setOf(
                "Bambu Lab",
                "Creality",
                "Elegoo",
                "Flashforge",
                "Anycubic",
                "Prusa",
                "Sovol"
            ),
            PrinterCatalog.brands().toSet()
        )
    }

    @Test
    fun catalogModelsCanBeFilteredByBrand() {
        val models = PrinterCatalog.modelsByBrand(
            "Bambu Lab"
        )

        assertEquals(6, models.size)

        assertTrue(
            models.all { it.brand == "Bambu Lab" }
        )
    }

    @Test
    fun unknownModelReturnsNull() {
        assertNull(
            PrinterCatalog.findById("unknown-model")
        )
    }

    @Test
    fun unknownPowerIsNotInvented() {
        val printer = assertNotNull(
            PrinterCatalog.findById(
                "creality-ender-3-v3-se"
            )
        )

        assertNull(
            printer.suggestedAveragePowerWatts
        )
    }

    @Test
    fun suggestedPowerIsAlwaysPositive() {
        assertTrue(
            PrinterCatalog.models.all {
                it.suggestedAveragePowerWatts == null ||
                        it.suggestedAveragePowerWatts > 0
            }
        )
    }


    @Test
    fun bambuA1HasOfficialMaximumPowerSpecification() {
        val printer = assertNotNull(
            PrinterCatalog.findById("bambu-a1")
        )

        assertEquals(1300, printer.maxPowerWatts)
        assertEquals(220, printer.maxPowerAtVoltageVolts)
        assertEquals(95, printer.suggestedAveragePowerWatts)
    }

}
