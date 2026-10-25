
package com.dannyrodrygues.printworth.presentation.about

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import org.jetbrains.compose.resources.painterResource
import printworth.shared.generated.resources.Res
import printworth.shared.generated.resources.printworth_icon

/**
 * Tela educativa e informativa do PrintWorth.
 *
 * Os assuntos são apresentados em formato acordeão.
 * Apenas uma seção permanece expandida por vez.
 *
 * Não executa cálculos financeiros.
 */
@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    val colors = MaterialTheme.colorScheme
    val uriHandler = LocalUriHandler.current

    // Guarda qual assunto está aberto.
    // Null indica que todos estão recolhidos.
    var expandedSection by rememberSaveable {
        mutableStateOf<String?>(null)
    }

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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Cabeçalho oficial do PrintWorth.
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
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
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
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Sobre o PrintWorth",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        Text(
            text = "Conheça seus custos. " +
                    "Valorize suas impressões.",
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurfaceVariant
        )

        Text(
            text = "Selecione um assunto abaixo " +
                    "para conhecer os detalhes.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant
        )

        // 1. Funcionamento geral.
        AboutSection(
            title = "Como funciona a calculadora?",
            expanded = expandedSection == "calculator",
            onToggle = {
                expandedSection = if (
                    expandedSection == "calculator"
                ) null else "calculator"
            }
        ) {

            Text(
                text = "O PrintWorth ajuda a estimar " +
                        "quanto custa produzir uma peça em " +
                        "impressão 3D e qual preço cobrar " +
                        "para atingir o lucro desejado."
            )

            Text(
                text = "O cálculo considera o filamento, " +
                        "a energia elétrica, o desgaste da " +
                        "impressora e a embalagem."
            )

            Text(
                text = "Você também pode informar uma " +
                        "comissão do ponto de venda."
            )
        }

        // 2. Lucro sobre o custo.
        AboutSection(
            title = "Lucro sobre o custo",
            expanded = expandedSection == "markup",
            onToggle = {
                expandedSection = if (
                    expandedSection == "markup"
                ) null else "markup"
            }
        ) {

            Text(
                text = "Esse método acrescenta um " +
                        "percentual ao custo de produção."
            )

            Text(
                text = "Exemplo:",
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Custo da peça: R$ 20,00\n" +
                        "Lucro desejado: 100%\n" +
                        "Preço de venda: R$ 40,00\n" +
                        "Lucro estimado: R$ 20,00"
            )

            Text(
                text = "Nesse método, 100% de lucro " +
                        "significa ganhar um valor igual " +
                        "ao custo da peça."
            )

            Text(
                text = "É possível informar 100%, " +
                        "150%, 200% ou mais, respeitando " +
                        "os limites numéricos."
            )
        }

        // 3. Margem sobre a venda.
        AboutSection(
            title = "Margem sobre a venda",
            expanded = expandedSection == "margin",
            onToggle = {
                expandedSection = if (
                    expandedSection == "margin"
                ) null else "margin"
            }
        ) {

            Text(
                text = "Esse método determina qual " +
                        "parte do preço de venda deve " +
                        "sobrar após os custos e a comissão."
            )

            Text(
                text = "Exemplo:",
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Custo da peça: R$ 20,00\n" +
                        "Margem desejada: 50%\n" +
                        "Preço de venda: R$ 40,00\n" +
                        "Lucro estimado: R$ 20,00"
            )

            Text(
                text = "Dos R$ 40,00 recebidos, " +
                        "R$ 20,00 correspondem ao custo " +
                        "e R$ 20,00 ao lucro estimado."
            )

            Text(
                text = "Por isso, a margem sobre " +
                        "a venda é de 50%."
            )

            Text(
                text = "Uma margem de 100% não é " +
                        "possível quando existe custo " +
                        "de produção."
            )
        }

        // 4. Comparação de métodos.
        AboutSection(
            title = "Qual é a diferença?",
            expanded = expandedSection == "difference",
            onToggle = {
                expandedSection = if (
                    expandedSection == "difference"
                ) null else "difference"
            }
        ) {

            Text(
                text = "Sobre o custo: compara o lucro " +
                        "com o dinheiro gasto para produzir."
            )

            Text(
                text = "Sobre a venda: compara o lucro " +
                        "com o valor recebido pela venda."
            )

            Text(
                text = "Uma peça que custa R$ 20,00 " +
                        "e é vendida por R$ 40,00 possui " +
                        "100% de lucro sobre o custo e " +
                        "50% de margem sobre a venda."
            )
        }

        // 5. Comissão do ponto de venda.
        AboutSection(
            title = "Comissão do ponto de venda",
            expanded = expandedSection == "commission",
            onToggle = {
                expandedSection = if (
                    expandedSection == "commission"
                ) null else "commission"
            }
        ) {

            Text(
                text = "A comissão é um percentual " +
                        "cobrado sobre o preço final " +
                        "de venda."
            )

            Text(
                text = "Quando a comissão está " +
                        "ativada, o PrintWorth ajusta " +
                        "o preço sugerido para considerar " +
                        "esse desconto."
            )

            Text(
                text = "No método Sobre a venda, " +
                        "a soma da margem desejada " +
                        "com a comissão precisa ser " +
                        "inferior a 100%."
            )
        }

        // 6. Limitações.
        AboutSection(
            title = "O que o cálculo não inclui?",
            expanded = expandedSection == "limitations",
            onToggle = {
                expandedSection = if (
                    expandedSection == "limitations"
                ) null else "limitations"
            }
        ) {

            Text(
                text = "O lucro apresentado é uma " +
                        "estimativa baseada nos custos " +
                        "informados."
            )

            Text(
                text = "Nesta versão, impostos, " +
                        "mão de obra, frete, aluguel " +
                        "e eventuais falhas de impressão " +
                        "não são incluídos automaticamente."
            )

            Text(
                text = "Por isso, o lucro estimado " +
                        "não deve ser confundido com " +
                        "o lucro líquido da empresa."
            )
        }


// Moedas disponíveis no aplicativo.
        AboutSection(
            title = "Moedas disponíveis",
            expanded = expandedSection == "currencies",
            onToggle = {
                expandedSection = if (
                    expandedSection == "currencies"
                ) null else "currencies"
            }
        ) {

            Text(
                text = "O PrintWorth permite calcular os custos " +
                        "e preços de venda em três moedas:"
            )

            Text(
                text = "• Real brasileiro (R$)\n" +
                        "• Dólar americano (US$)\n" +
                        "• Euro (€)",
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Todos os valores devem ser informados " +
                        "diretamente na moeda selecionada. " +
                        "O custo total, o preço sugerido e o lucro " +
                        "estimado serão apresentados nessa mesma moeda."
            )

            Text(
                text = "O aplicativo não realiza conversão cambial.",
                fontWeight = FontWeight.SemiBold,
                color = colors.secondary
            )

            Text(
                text = "Ao alterar a moeda, os valores monetários " +
                        "já preenchidos são limpos após confirmação, " +
                        "evitando misturar moedas diferentes. " +
                        "Os demais dados da impressão são preservados."
            )
        }


        // 7. Informações do projeto.
        AboutSection(
            title = "Sobre o projeto",
            expanded = expandedSection == "project",
            onToggle = {
                expandedSection = if (
                    expandedSection == "project"
                ) null else "project"
            }
        ) {

            Text(
                text = "O PrintWorth é um aplicativo " +
                        "desenvolvido para auxiliar " +
                        "profissionais, empreendedores " +
                        "e entusiastas da impressão 3D."
            )

            Text(
                text = "Desenvolvido em Kotlin, " +
                        "utilizando Kotlin Multiplatform, " +
                        "Compose Multiplatform e Material 3."
            )

            Text(
                text = "Desenvolvedora: Dani Rodrigues.",
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Contato: devdanyoliver@gmail.com",
                color = colors.secondary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    uriHandler.openUri(
                        "mailto:devdanyoliver@gmail.com"
                    )
                }
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {

            Text(
                text = "Voltar",
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}

/**
 * Seção expansível reutilizável.
 *
 * Quando fechada, exibe somente o título.
 * Quando aberta, apresenta o conteúdo educativo.
 */
@Composable
private fun AboutSection(
    title: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.surface,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (expanded) {
                colors.primary.copy(alpha = 0.55f)
            } else {
                colors.outline.copy(alpha = 0.25f)
            }
        )
    ) {

        Column {

            // Área clicável do sumário.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        stateDescription = if (expanded) {
                            "Expandido"
                        } else {
                            "Recolhido"
                        }
                    }
                    .clickable(onClick = onToggle)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 18.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )

                // Indicador de expandir/recolher.
                Text(
                    text = if (expanded) "−" else "+",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.primary
                )
            }

            // O conteúdo só aparece quando
            // o assunto está selecionado.
            if (expanded) {

                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    ),
                    color = colors.outline.copy(alpha = 0.2f)
                )

                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 14.dp,
                        bottom = 18.dp
                    ),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    content()
                }
            }
        }
    }
}
