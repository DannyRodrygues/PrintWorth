
package com.dannyrodrygues.printworth.domain.model

/**
 * Representa uma impressora 3D cadastrada.
 *
 * @property id Identificador único da impressora.
 * @property name Nome ou modelo da impressora.
 * @property averagePowerWatts Potência média estimada em watts.
 */
data class Printer(
    val id: String,
    val name: String,
    val averagePowerWatts: Int
)
