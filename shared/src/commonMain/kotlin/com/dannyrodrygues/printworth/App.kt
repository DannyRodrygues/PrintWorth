
package com.dannyrodrygues.printworth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.dannyrodrygues.printworth.designsystem.theme.PrintWorthTheme
import com.dannyrodrygues.printworth.domain.model.Printer
import com.dannyrodrygues.printworth.presentation.about.AboutScreen
import com.dannyrodrygues.printworth.presentation.calculator.CalculatorScreen
import com.dannyrodrygues.printworth.presentation.calculator.CalculatorViewModel
import com.dannyrodrygues.printworth.presentation.result.ResultScreen

import org.jetbrains.compose.resources.painterResource
import printworth.shared.generated.resources.Res
import printworth.shared.generated.resources.printworth_icon

import com.dannyrodrygues.printworth.domain.repository.PrinterRepository
import com.dannyrodrygues.printworth.presentation.printer.PrinterManagementScreen
import com.dannyrodrygues.printworth.presentation.printer.PrinterManagementViewModel

import androidx.compose.runtime.remember
import com.dannyrodrygues.printworth.domain.catalog.PrinterCatalog

import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

/**
 * Telas principais do aplicativo.
 */
private const val SCREEN_HOME = "home"
private const val SCREEN_CALCULATOR = "calculator"
private const val SCREEN_RESULT = "result"
private const val SCREEN_ABOUT = "about"

private const val SCREEN_PRINTERS = "printers"

/**
 * Ponto de entrada da interface do PrintWorth.
 *
 * Aplica o tema e organiza a navegação.
 *
 * As regras de cálculo permanecem
 * nas camadas de domínio e apresentação.
 */
@Composable
fun App(
    printerRepository: PrinterRepository
) {

    var currentScreen by rememberSaveable {
        mutableStateOf(SCREEN_HOME)
    }

    PrintWorthTheme {

        val colors = MaterialTheme.colorScheme

        when (currentScreen) {

            SCREEN_HOME -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.background)
                        .safeContentPadding()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        // Logotipo oficial
                        Image(
                            painter = painterResource(
                                Res.drawable.printworth_icon
                            ),
                            contentDescription = "Ícone PrintWorth",
                            modifier = Modifier
                                .height(82.dp)
                                .fillMaxWidth(0.28f),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        // Nome da marca
                        Text(
                            text = buildAnnotatedString {

                                withStyle(
                                    SpanStyle(
                                        color = colors.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Print")
                                }

                                withStyle(
                                    SpanStyle(
                                        color = colors.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Worth")
                                }
                            },
                            fontSize = 26.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        // Slogan
                        Text(
                            text = "Conheça seus custos.\nValorize suas impressões.",
                            color = colors.onBackground,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier = Modifier.height(28.dp)
                        )

                        // Card principal da calculadora
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(colors.surface)
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 18.dp
                                ),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.Center
                        ) {

                            Text(
                                text = "Calculadora de Impressão 3D",
                                color = colors.onSurface,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            Text(
                                text = "Calcule custos de produção e descubra o preço ideal para suas peças.",
                                color = colors.onSurfaceVariant,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                textAlign = TextAlign.Center
                            )

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Button(
                                onClick = {
                                    currentScreen =
                                        SCREEN_CALCULATOR
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.primary,
                                    contentColor = colors.onPrimary
                                )
                            ) {
                                Text(
                                    text = "Iniciar cálculo →",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        // Novo acesso à seção educativa
                        TextButton(
                            onClick = {
                                currentScreen = SCREEN_ABOUT
                            }
                        ) {
                            Text(
                                text = "Sobre o PrintWorth  ⓘ",
                                fontSize = 14.sp,
                                color = colors.secondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            SCREEN_ABOUT -> {

                // Tela educativa e informativa
                AboutScreen(
                    onBack = {
                        currentScreen = SCREEN_HOME
                    }
                )
            }


            SCREEN_PRINTERS -> {

                val printerManagementViewModel:
                        PrinterManagementViewModel = viewModel(
                    initializer = {
                        PrinterManagementViewModel(
                            repository = printerRepository
                        )
                    }
                )

                PrinterManagementScreen(
                    viewModel = printerManagementViewModel,

                    onBack = {
                        currentScreen = SCREEN_CALCULATOR
                    }
                )
            }


            else -> {

                // ViewModel compartilhado entre
                // o formulário e o resultado.
                val calculatorViewModel: CalculatorViewModel =
                    viewModel()

                val result = calculatorViewModel
                    .calculationResult
                    .collectAsState()
                    .value

                if (
                    currentScreen == SCREEN_RESULT &&
                    result != null
                ) {

                    ResultScreen(
                        result = result,

                        onEdit = {
                            // Mantém os dados preenchidos.
                            currentScreen =
                                SCREEN_CALCULATOR
                        },

                        onNewCalculation = {
                            calculatorViewModel.resetForm()

                            currentScreen =
                                SCREEN_CALCULATOR
                        }
                    )

                } else {

                    /*
                     * Impressora temporária de demonstração.
                     *
                     * Será substituída pelo cadastro local.
                     * 150 W é um valor ilustrativo.
                     */

// Modelos pré-cadastrados, disponíveis offline.
                    val catalogPrinters = remember {
                        PrinterCatalog.models.map { model ->
                            Printer(
                                id = model.id,
                                name = model.displayName,

                                // Zero representa apenas ausência de
                                // estimativa nesta lista temporária.
                                // O formulário exigirá uma potência
                                // positiva antes de realizar o cálculo.
                                averagePowerWatts =
                                    model.suggestedAveragePowerWatts ?: 0
                            )
                        }
                    }


// Observa as impressoras pessoais salvas no DataStore.
// Null significa que ainda não terminou o primeiro carregamento.
                    val personalPrintersFlow: Flow<List<Printer>?> =
                        remember(printerRepository) {
                            printerRepository.observePrinters()
                        }

                    val personalPrinters by personalPrintersFlow.collectAsState(
                        initial = null
                    )

// Une os modelos do catálogo às impressoras pessoais.
                    val availablePrinters =
                        catalogPrinters + personalPrinters.orEmpty()

// Sincroniza a seleção quando os cadastros mudam.
                    LaunchedEffect(
                        personalPrinters,
                        catalogPrinters
                    ) {
                        if (personalPrinters != null) {
                            calculatorViewModel.synchronizeSelectedPrinter(
                                availablePrinters
                            )
                        }
                    }



                    CalculatorScreen(
                        viewModel = calculatorViewModel,
                        printers = availablePrinters,

                        onBack = {
                            currentScreen = SCREEN_HOME
                        },
                        onManagePrinters = {
                            currentScreen = SCREEN_PRINTERS
                        },

                        onCalculated = {
                            currentScreen = SCREEN_RESULT
                        }
                    )
                }
            }
        }
    }
}
