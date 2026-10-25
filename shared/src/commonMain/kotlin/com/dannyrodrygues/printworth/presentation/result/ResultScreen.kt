
package com.dannyrodrygues.printworth.presentation.result

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dannyrodrygues.printworth.domain.model.PrintCalculationResult
import org.jetbrains.compose.resources.painterResource
import printworth.shared.generated.resources.Res
import printworth.shared.generated.resources.printworth_icon
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.ui.geometry.Offset
import com.dannyrodrygues.printworth.domain.model.CalculationCurrency
import com.dannyrodrygues.printworth.presentation.format.MoneyFormatter

/**
 * Apresenta os resultados financeiros da impressão.
 *
 * Recebe dados já calculados pela camada de domínio.
 * Não executa fórmulas financeiras.
 */

@Composable
fun ResultScreen(
    result: PrintCalculationResult,
    currency: CalculationCurrency,
    onEdit: () -> Unit,
    onNewCalculation: () -> Unit,
    modifier: Modifier = Modifier
) {

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .safeContentPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        PrintWorthHeader()

        ResultStepIndicator()

        TextButton(
            onClick = onEdit
        ) {
            Text("← Voltar para edição")
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Resultado",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground
            )

            Text(
                text = result.pieceName
                    ?: "Sua impressão 3D",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant
            )
        }


        FinancialHighlightCard(
            title = "Preço sugerido",
            amountInCents = result.suggestedSalePrice.cents,
            currency = currency,
            gradientColors = listOf(
                Color(0xFF103D46),
                Color(0xFF07333B)
            )
        )

        FinancialHighlightCard(
            title = "Lucro estimado",
            amountInCents = result.estimatedProfit.cents,
            currency = currency,
            gradientColors = listOf(
                Color(0xFF007F73),
                Color(0xFF00958B)
            ),
            subtitle = result.effectiveMarginBasisPoints
                ?.let {
                    "Margem sobre a venda: ${formatPercentage(it)}"
                }
                ?: "Margem sobre a venda: não se aplica"
        )


        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = colors.surface,
            border = BorderStroke(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.25f)
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "Detalhamento dos custos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )

                ResultCostRow(
                    label = "Filamento",
                    amountInCents = result.filamentCost.cents,
                    currency = currency
                )

                ResultCostRow(
                    label = "Energia",
                    amountInCents = result.energyCost.cents,
                    currency = currency
                )

                ResultCostRow(
                    label = "Desgaste da impressora",
                    amountInCents = result.printerWearCost.cents,
                    currency = currency
                )

                ResultCostRow(
                    label = "Embalagem",
                    amountInCents = result.packagingCost.cents,
                    currency = currency
                )

                HorizontalDivider(
                    color = colors.outline.copy(alpha = 0.3f)
                )

                ResultCostRow(
                    label = "Custo base",
                    amountInCents = result.baseCost.cents,
                    currency = currency,
                    emphasized = true
                )

                HorizontalDivider(
                    color = colors.outline.copy(alpha = 0.3f)
                )

                ResultCostRow(
                    label = "Comissão do ponto",
                    amountInCents = result.commissionAmount.cents,
                    currency = currency
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "Valores estimados com base nos custos " +
                            "informados. Comissão e valores monetários " +
                            "apresentados após arredondamento.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )
            }
        }

        OutlinedButton(
            onClick = onEdit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Editar cálculo",
                modifier = Modifier.padding(vertical = 7.dp)
            )
        }

        // Persistência de cálculos ainda não implementada.
        Button(
            onClick = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Salvar cálculo (em breve)",
                modifier = Modifier.padding(vertical = 7.dp)
            )
        }

        TextButton(
            onClick = onNewCalculation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Novo cálculo")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}

/**
 * Cabeçalho inspirado no protótipo oficial.
 */
@Composable
private fun PrintWorthHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF103D46),
                        Color(0xFF07515A)
                    )
                )
            )
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Image(
            painter = painterResource(
                Res.drawable.printworth_icon
            ),
            contentDescription = "Ícone PrintWorth",
            modifier = Modifier.size(42.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "PrintWorth",
            color = Color.White,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


/**
 * Indicador de progresso da tela de resultados.
 *
 * Mantém o padrão visual das etapas:
 * 1. Impressão
 * 2. Custos
 * 3. Resultado
 */
@Composable
private fun ResultStepIndicator() {

    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
        ) {

            val y = size.height / 2f
            val firstCenter = size.width / 6f
            val lastCenter = size.width * 5f / 6f

            // Linha de conexão entre as três etapas.
            drawLine(
                color = colors.outline.copy(alpha = 0.5f),
                start = Offset(firstCenter, y),
                end = Offset(lastCenter, y),
                strokeWidth = 2.dp.toPx()
            )

            // Na tela de resultados, todas as etapas
            // já foram alcançadas.
            drawLine(
                color = colors.primary,
                start = Offset(firstCenter, y),
                end = Offset(lastCenter, y),
                strokeWidth = 2.dp.toPx()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            ResultStep(
                number = 1,
                title = "Impressão",
                selected = false,
                completed = true,
                modifier = Modifier.weight(1f)
            )

            ResultStep(
                number = 2,
                title = "Custos",
                selected = false,
                completed = true,
                modifier = Modifier.weight(1f)
            )

            ResultStep(
                number = 3,
                title = "Resultado",
                selected = true,
                completed = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Representação visual de cada etapa.
 *
 * Etapa atual: círculo preenchido.
 * Etapa concluída: círculo com contorno turquesa.
 * Etapa futura: círculo com contorno neutro.
 */
@Composable
private fun ResultStep(
    number: Int,
    title: String,
    selected: Boolean,
    completed: Boolean,
    modifier: Modifier = Modifier
) {

    val colors = MaterialTheme.colorScheme
    val highlighted = selected || completed

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = if (selected) {
                        colors.primary
                    } else {
                        colors.surface
                    },
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = if (highlighted) {
                        colors.primary
                    } else {
                        colors.outline
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = number.toString(),
                color = when {
                    selected -> colors.onPrimary
                    completed -> colors.primary
                    else -> colors.onSurfaceVariant
                },
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = if (highlighted) {
                colors.primary
            } else {
                colors.onSurfaceVariant
            },
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            textAlign = TextAlign.Center
        )
    }
}


/**
 * Cartão de destaque para preço e lucro.
 */

@Composable
private fun FinancialHighlightCard(
    title: String,
    amountInCents: Long,
    currency: CalculationCurrency,
    gradientColors: List<Color>,
    subtitle: String? = null
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(gradientColors)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = MoneyFormatter.format(
                amountInCents = amountInCents,
                currency = currency
            ),
            color = Color.White,
            fontSize = 38.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.Bold
        )

        if (subtitle != null) {
            Text(
                text = subtitle,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


/**
 * Uma linha do detalhamento financeiro.
 */

@Composable
private fun ResultCostRow(
    label: String,
    amountInCents: Long,
    currency: CalculationCurrency,
    emphasized: Boolean = false
) {

    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = colors.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (emphasized) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )

        Text(
            text = MoneyFormatter.format(
                amountInCents = amountInCents,
                currency = currency
            ),
            color = colors.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (emphasized) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}

/**
 * Formata pontos-base como percentual.
 *
 * Exemplo:
 * 5000 -> 50%
 * 4550 -> 45,50%
 */
private fun formatPercentage(
    basisPoints: Int
): String {

    val whole = basisPoints / 100
    val fraction = basisPoints % 100

    return if (fraction == 0) {
        "$whole%"
    } else {
        "$whole,${fraction.toString().padStart(2, '0')}%"
    }
}
