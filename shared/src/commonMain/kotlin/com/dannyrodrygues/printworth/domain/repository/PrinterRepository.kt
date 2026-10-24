
package com.dannyrodrygues.printworth.domain.repository

import com.dannyrodrygues.printworth.domain.model.Printer
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de persistência das impressoras pessoais.
 *
 * O catálogo de fábrica não é modificado por este
 * repositório.
 *
 * A implementação concreta armazenará os dados
 * localmente no dispositivo.
 */
interface PrinterRepository {

    /**
     * Observa as impressoras pessoais cadastradas.
     */
    fun observePrinters(): Flow<List<Printer>>

    /**
     * Cadastra uma impressora pessoal.
     *
     * O repositório cria e devolve um ID único.
     */
    suspend fun addPrinter(
        name: String,
        averagePowerWatts: Int
    ): Printer

    /**
     * Atualiza uma impressora pessoal existente.
     */
    suspend fun updatePrinter(printer: Printer)

    /**
     * Exclui uma impressora pessoal pelo ID.
     */
    suspend fun deletePrinter(id: String)
}
