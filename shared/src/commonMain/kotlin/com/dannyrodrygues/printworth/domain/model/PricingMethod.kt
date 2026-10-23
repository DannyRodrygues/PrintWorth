
package com.dannyrodrygues.printworth.domain.model

/**
 * Método utilizado para definir o preço de venda.
 *
 * COST_MARKUP:
 * Acréscimo percentual sobre o custo de produção.
 *
 * SALE_MARGIN:
 * Margem desejada sobre o preço final de venda,
 * após o custo base e a comissão.
 */
enum class PricingMethod {
    COST_MARKUP,
    SALE_MARGIN
}
