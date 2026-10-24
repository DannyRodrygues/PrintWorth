
package com.dannyrodrygues.printworth.data.repository

import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import com.dannyrodrygues.printworth.domain.model.Printer
import com.dannyrodrygues.printworth.domain.repository.PrinterRepository

import java.util.UUID

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Uma única instância de DataStore para o arquivo
 * de impressoras pessoais do PrintWorth.
 */
private val Context.printerDataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "printworth_personal_printers"
)

/**
 * Implementação Android do PrinterRepository.
 *
 * Responsabilidades:
 * - Cadastrar impressoras pessoais.
 * - Persistir nome e potência média.
 * - Atualizar informações.
 * - Excluir impressoras pessoais.
 * - Observar alterações por Flow.
 *
 * O catálogo oficial não é alterado.
 */
class AndroidPrinterRepository(
    context: Context
) : PrinterRepository {

    private val dataStore =
        context.applicationContext.printerDataStore

    private val printerIdsKey =
        stringSetPreferencesKey("personal_printer_ids")

    private fun printerNameKey(id: String) =
        stringPreferencesKey("printer_${id}_name")

    private fun printerPowerKey(id: String) =
        intPreferencesKey("printer_${id}_power_watts")

    /**
     * Observa as impressoras cadastradas.
     *
     * A lista será atualizada quando ocorrerem
     * alterações no DataStore.
     */
    override fun observePrinters(): Flow<List<Printer>> {

        return dataStore.data.map { preferences ->

            val ids =
                preferences[printerIdsKey] ?: emptySet()

            ids.mapNotNull { id ->

                val name =
                    preferences[printerNameKey(id)]

                val power =
                    preferences[printerPowerKey(id)]

                if (
                    name.isNullOrBlank() ||
                    power == null ||
                    power <= 0
                ) {
                    null
                } else {
                    Printer(
                        id = id,
                        name = name,
                        averagePowerWatts = power
                    )
                }

            }.sortedWith(
                compareBy<Printer>(
                    { it.name.lowercase() },
                    { it.id }
                )
            )
        }
    }

    /**
     * Cadastra uma impressora pessoal.
     *
     * Cada impressora recebe um identificador
     * exclusivo, permitindo cadastrar duas
     * máquinas do mesmo modelo.
     */
    override suspend fun addPrinter(
        name: String,
        averagePowerWatts: Int
    ): Printer {

        val normalizedName = name.trim()

        validatePrinter(
            normalizedName,
            averagePowerWatts
        )

        val printer = Printer(
            id = "custom-${UUID.randomUUID()}",
            name = normalizedName,
            averagePowerWatts = averagePowerWatts
        )

        dataStore.edit { preferences ->

            val existingIds =
                preferences[printerIdsKey] ?: emptySet()

            preferences[printerIdsKey] =
                existingIds + printer.id

            preferences[printerNameKey(printer.id)] =
                printer.name

            preferences[printerPowerKey(printer.id)] =
                printer.averagePowerWatts
        }

        return printer
    }

    /**
     * Atualiza uma impressora pessoal existente.
     *
     * Não permite editar entradas do catálogo fixo.
     */
    override suspend fun updatePrinter(
        printer: Printer
    ) {

        validatePrinter(
            printer.name,
            printer.averagePowerWatts
        )

        require(printer.id.startsWith("custom-")) {
            "Somente impressoras pessoais podem ser editadas."
        }

        dataStore.edit { preferences ->

            val existingIds =
                preferences[printerIdsKey] ?: emptySet()

            require(printer.id in existingIds) {
                "Impressora não encontrada."
            }

            preferences[printerNameKey(printer.id)] =
                printer.name.trim()

            preferences[printerPowerKey(printer.id)] =
                printer.averagePowerWatts
        }
    }

    /**
     * Remove uma impressora pessoal.
     *
     * Os modelos do catálogo não são afetados.
     */
    override suspend fun deletePrinter(
        id: String
    ) {

        require(id.startsWith("custom-")) {
            "Somente impressoras pessoais podem ser removidas."
        }

        dataStore.edit { preferences ->

            val existingIds =
                preferences[printerIdsKey] ?: emptySet()

            require(id in existingIds) {
                "Impressora não encontrada."
            }

            preferences[printerIdsKey] =
                existingIds - id

            preferences.remove(
                printerNameKey(id)
            )

            preferences.remove(
                printerPowerKey(id)
            )
        }
    }

    /**
     * Validação básica do cadastro.
     */
    private fun validatePrinter(
        name: String,
        averagePowerWatts: Int
    ) {

        require(name.isNotBlank()) {
            "O nome da impressora é obrigatório."
        }

        require(name.length <= 80) {
            "O nome deve ter no máximo 80 caracteres."
        }

        require(averagePowerWatts > 0) {
            "A potência média deve ser maior que zero."
        }
    }
}
