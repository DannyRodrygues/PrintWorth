
package com.dannyrodrygues.printworth.domain.catalog

/**
 * Modelo pré-cadastrado no catálogo PrintWorth.
 *
 * O consumo médio sugerido pode ser null quando
 * não existe referência suficientemente confiável.
 *
 * Os valores de potência são estimativas para PLA,
 * não a potência máxima da fonte da impressora.
 */

data class PrinterCatalogEntry(
    val id: String,
    val brand: String,
    val model: String,

    // Referência de consumo médio para o cálculo.
    val suggestedAveragePowerWatts: Int? = null,

    // Potência máxima declarada pelo fabricante.
    val maxPowerWatts: Int? = null,

    // Tensão correspondente à potência máxima.
    val maxPowerAtVoltageVolts: Int? = null
) {
    val displayName: String
        get() = "$brand $model"

    init {
        require(id.isNotBlank())
        require(brand.isNotBlank())
        require(model.isNotBlank())

        require(
            suggestedAveragePowerWatts == null ||
                    suggestedAveragePowerWatts > 0
        )

        require(
            (maxPowerWatts == null) ==
                    (maxPowerAtVoltageVolts == null)
        ) {
            "Potência máxima e tensão devem ser informadas juntas."
        }

        require(
            maxPowerWatts == null || maxPowerWatts > 0
        )

        require(
            maxPowerAtVoltageVolts == null ||
                    maxPowerAtVoltageVolts > 0
        )
    }
}


/**
 * Catálogo inicial de impressoras 3D FDM.
 *
 * Os IDs são estáveis para permitir evolução futura.
 * A lista é fixa e não depende de internet.
 *
 * O cadastro de impressoras pessoais será mantido
 * separadamente, por meio de PrinterRepository.
 */
object PrinterCatalog {

    val models: List<PrinterCatalogEntry> = listOf(

        // BAMBU LAB

        PrinterCatalogEntry(
            id = "bambu-a1",
            brand = "Bambu Lab",
            model = "A1",

            // Referência de consumo médio durante impressão PLA.
            suggestedAveragePowerWatts = 95,

            // Especificação elétrica oficial em 220 V.
            maxPowerWatts = 1300,
            maxPowerAtVoltageVolts = 220
        ),


        PrinterCatalogEntry(
            id = "bambu-a1-mini",
            brand = "Bambu Lab",
            model = "A1 Mini",
            suggestedAveragePowerWatts = 80
        ),

        PrinterCatalogEntry(
            id = "bambu-p1s",
            brand = "Bambu Lab",
            model = "P1S",
            suggestedAveragePowerWatts = 105
        ),

        PrinterCatalogEntry(
            id = "bambu-p1p",
            brand = "Bambu Lab",
            model = "P1P"
        ),

        PrinterCatalogEntry(
            id = "bambu-p2s",
            brand = "Bambu Lab",
            model = "P2S"
        ),

        PrinterCatalogEntry(
            id = "bambu-x1-carbon",
            brand = "Bambu Lab",
            model = "X1 Carbon",
            suggestedAveragePowerWatts = 105
        ),

        // CREALITY
        PrinterCatalogEntry(
            id = "creality-ender-3",
            brand = "Creality",
            model = "Ender-3"
        ),

        PrinterCatalogEntry(
            id = "creality-ender-3-pro",
            brand = "Creality",
            model = "Ender-3 Pro"
        ),

        PrinterCatalogEntry(
            id = "creality-ender-3-v2",
            brand = "Creality",
            model = "Ender-3 V2"
        ),

        PrinterCatalogEntry(
            id = "creality-ender-3-v3-se",
            brand = "Creality",
            model = "Ender-3 V3 SE"
        ),

        PrinterCatalogEntry(
            id = "creality-ender-3-v3-ke",
            brand = "Creality",
            model = "Ender-3 V3 KE"
        ),

        PrinterCatalogEntry(
            id = "creality-ender-3-v3",
            brand = "Creality",
            model = "Ender-3 V3"
        ),

        PrinterCatalogEntry(
            id = "creality-k1",
            brand = "Creality",
            model = "K1"
        ),

        PrinterCatalogEntry(
            id = "creality-k1c",
            brand = "Creality",
            model = "K1C"
        ),

        // ELEGOO
        PrinterCatalogEntry(
            id = "elegoo-neptune-3-pro",
            brand = "Elegoo",
            model = "Neptune 3 Pro"
        ),

        PrinterCatalogEntry(
            id = "elegoo-neptune-4",
            brand = "Elegoo",
            model = "Neptune 4"
        ),

        PrinterCatalogEntry(
            id = "elegoo-neptune-4-pro",
            brand = "Elegoo",
            model = "Neptune 4 Pro"
        ),

        PrinterCatalogEntry(
            id = "elegoo-centauri-carbon",
            brand = "Elegoo",
            model = "Centauri Carbon"
        ),

        // FLASHFORGE
        PrinterCatalogEntry(
            id = "flashforge-adventurer-5m",
            brand = "Flashforge",
            model = "Adventurer 5M"
        ),

        PrinterCatalogEntry(
            id = "flashforge-adventurer-5m-pro",
            brand = "Flashforge",
            model = "Adventurer 5M Pro"
        ),

        // ANYCUBIC
        PrinterCatalogEntry(
            id = "anycubic-kobra-3",
            brand = "Anycubic",
            model = "Kobra 3"
        ),

        PrinterCatalogEntry(
            id = "anycubic-kobra-s1",
            brand = "Anycubic",
            model = "Kobra S1"
        ),

        // PRUSA
        PrinterCatalogEntry(
            id = "prusa-i3-mk3s-plus",
            brand = "Prusa",
            model = "i3 MK3S+"
        ),

        PrinterCatalogEntry(
            id = "prusa-mk4s",
            brand = "Prusa",
            model = "MK4S"
        ),

        PrinterCatalogEntry(
            id = "prusa-mini-plus",
            brand = "Prusa",
            model = "MINI+"
        ),

        // SOVOL
        PrinterCatalogEntry(
            id = "sovol-sv06-ace",
            brand = "Sovol",
            model = "SV06 ACE"
        )
    )

    /**
     * Localiza um modelo pelo ID estável.
     */
    fun findById(id: String): PrinterCatalogEntry? {
        return models.firstOrNull { it.id == id }
    }

    /**
     * Lista as marcas disponíveis no catálogo.
     */
    fun brands(): List<String> {
        return models
            .map { it.brand }
            .distinct()
            .sorted()
    }

    /**
     * Retorna os modelos de uma marca.
     */
    fun modelsByBrand(
        brand: String
    ): List<PrinterCatalogEntry> {
        return models
            .filter { it.brand == brand }
            .sortedBy { it.model }
    }
}
