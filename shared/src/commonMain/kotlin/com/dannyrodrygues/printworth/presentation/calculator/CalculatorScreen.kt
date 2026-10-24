
package com.dannyrodrygues.printworth.presentation.calculator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.dannyrodrygues.printworth.domain.model.Printer

import org.jetbrains.compose.resources.painterResource
import printworth.shared.generated.resources.Res
import printworth.shared.generated.resources.printworth_icon
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import com.dannyrodrygues.printworth.domain.model.PricingMethod

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Formulário dividido em duas etapas:
 *
 * 1. Impressão
 * 2. Custos e preço
 *
 * A terceira etapa (Resultado) pertence à ResultScreen.
 *
 * O ViewModel mantém todos os valores digitados.
 */
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    printers: List<Printer>,
    onBack: () -> Unit,
    onCalculated: () -> Unit,
    onManagePrinters: () -> Unit,
    modifier: Modifier = Modifier
) {

    var currentStep by rememberSaveable {
        mutableStateOf(1)
    }

    var showPricingHelp by rememberSaveable {
        mutableStateOf(false)
    }

    val form by viewModel.formState.collectAsState()
    val errors by viewModel.fieldErrors.collectAsState()
    val generalError by viewModel.generalError.collectAsState()

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .safeContentPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        CalculatorHeader()

        CalculatorStepIndicator(
            currentStep = currentStep
        )

        if (currentStep == 1) {

            // ETAPA 1 — IMPRESSÃO

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Dados da impressão",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )

                Text(
                    text = "Informe os dados do fatiador",
                    color = colors.onSurfaceVariant
                )
            }

            CalculatorTextField(
                title = "Nome da peça",
                value = form.pieceName,
                onValueChange = viewModel::onPieceNameChanged,
                keyboardType = KeyboardType.Text
            )

            PrinterSelector(
                printers = printers,
                selectedPrinterId = form.selectedPrinterId,
                error = errors[CalculatorFormField.PRINTER],
                onPrinterSelected = viewModel::onPrinterSelected,
                onManagePrinters = onManagePrinters
            )

            CalculatorTextField(
                title = "Filamento consumido",
                value = form.filamentWeightGrams,
                onValueChange = viewModel::onFilamentWeightChanged,
                unit = "g",
                hint = "Inclua suportes, brim e purga.",
                error = errors[CalculatorFormField.FILAMENT_WEIGHT]
            )

            CalculatorTextField(
                title = "Preço do filamento",
                value = form.filamentPricePerKg,
                onValueChange = viewModel::onFilamentPriceChanged,
                unit = "R$/kg",
                error = errors[CalculatorFormField.FILAMENT_PRICE]
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Tempo de impressão",
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onBackground
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    CalculatorTextField(
                        title = "Horas",
                        value = form.printHours,
                        onValueChange =
                            viewModel::onPrintHoursChanged,
                        unit = "h",
                        error = errors[
                            CalculatorFormField.PRINT_HOURS
                        ],
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        showTitle = false
                    )

                    CalculatorTextField(
                        title = "Minutos",
                        value = form.printMinutes,
                        onValueChange =
                            viewModel::onPrintMinutesChanged,
                        unit = "min",
                        error = errors[
                            CalculatorFormField.PRINT_MINUTES
                        ],
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        showTitle = false
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    currentStep = 2
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Continuar",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar à tela inicial")
            }

        } else {

            // ETAPA 2 — CUSTOS E PREÇO

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Custos e preço",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onBackground
                )

                Text(
                    text = "Defina seus custos e o lucro desejado",
                    color = colors.onSurfaceVariant
                )
            }

            CalculatorTextField(
                title = "Consumo médio da impressora",
                value = form.averagePowerWatts,
                onValueChange = viewModel::onAveragePowerChanged,
                unit = "W",
                keyboardType = KeyboardType.Number,
                hint = "Potência média utilizada na impressão.",
                error = errors[CalculatorFormField.AVERAGE_POWER]
            )

            CalculatorTextField(
                title = "Tarifa de energia",
                value = form.electricityRatePerKwh,
                onValueChange =
                    viewModel::onElectricityRateChanged,
                unit = "R$/kWh",
                error = errors[CalculatorFormField.ELECTRICITY_RATE]
            )

            CalculatorTextField(
                title = "Desgaste da impressora",
                value = form.printerWearPerHour,
                onValueChange = viewModel::onPrinterWearChanged,
                unit = "R$/h",
                error = errors[CalculatorFormField.PRINTER_WEAR]
            )

            CalculatorTextField(
                title = "Embalagem (opcional)",
                value = form.packagingCost,
                onValueChange = viewModel::onPackagingCostChanged,
                unit = "R$",
                error = errors[CalculatorFormField.PACKAGING]
            )



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Como deseja definir o lucro?",
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onBackground
                )

                IconButton(
                    onClick = {
                        showPricingHelp = true
                    },
                    modifier = Modifier.semantics {
                        contentDescription =
                            "Ajuda sobre os métodos de precificação"
                    }
                ) {
                    Text(
                        text = "ⓘ",
                        color = colors.secondary,
                        fontSize = 23.sp
                    )
                }
            }
            if (showPricingHelp) {

                AlertDialog(
                    onDismissRequest = {
                        showPricingHelp = false
                    },

                    title = {
                        Text("Como funciona o lucro?")
                    },

                    text = {

                        Column(
                            verticalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {

                            Text(
                                text = "Sobre o custo",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Acrescenta o percentual desejado " +
                                        "ao custo de produção.\n\n" +
                                        "Exemplo: uma peça custa R$ 20,00. " +
                                        "Com 100% sobre o custo, o preço " +
                                        "será R$ 40,00, sem comissão."
                            )

                            Text(
                                text = "Sobre a venda",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Define quanto do preço final " +
                                        "deve sobrar após descontar " +
                                        "os custos e a comissão.\n\n" +
                                        "Exemplo: uma peça custa R$ 20,00. " +
                                        "Para obter margem de 50%, " +
                                        "o preço será R$ 40,00, " +
                                        "sem comissão."
                            )

                            Text(
                                text = "Importante: no método Sobre " +
                                        "a venda, margem e comissão " +
                                        "somadas devem ser inferiores a 100%.",
                                color = MaterialTheme.colorScheme
                                    .onSurfaceVariant
                            )
                        }
                    },

                    confirmButton = {
                        TextButton(
                            onClick = {
                                showPricingHelp = false
                            }
                        ) {
                            Text("Entendi")
                        }
                    }
                )
            }


            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {

                val methods = listOf(
                    PricingMethod.COST_MARKUP to "Sobre o custo",
                    PricingMethod.SALE_MARGIN to "Sobre a venda"
                )

                methods.forEachIndexed { index, item ->

                    val method = item.first
                    val label = item.second

                    SegmentedButton(
                        selected = form.pricingMethod == method,
                        onClick = {
                            viewModel.onPricingMethodChanged(method)
                        },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = methods.size
                        ),
                        label = {
                            Text(label)
                        }
                    )
                }
            }

            CalculatorTextField(
                title = if (
                    form.pricingMethod == PricingMethod.COST_MARKUP
                ) {
                    "Lucro desejado sobre o custo"
                } else {
                    "Margem desejada sobre a venda"
                },

                value = form.profitMarginPercent,
                onValueChange = viewModel::onProfitMarginChanged,
                unit = "%",

                hint = if (
                    form.pricingMethod == PricingMethod.COST_MARKUP
                ) {
                    "100% significa dobrar o custo da peça, sem comissão."
                } else {
                    "A margem e a comissão somadas devem ficar abaixo de 100%."
                },

                error = errors[CalculatorFormField.PROFIT_MARGIN]
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = "Comissão do ponto de venda",
                        color = colors.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Ative quando houver comissão.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                }

                Switch(
                    checked = form.commissionEnabled,
                    onCheckedChange =
                        viewModel::onCommissionEnabledChanged
                )
            }

            if (form.commissionEnabled) {
                CalculatorTextField(
                    title = "Comissão do ponto de venda",
                    value = form.commissionPercent,
                    onValueChange =
                        viewModel::onCommissionPercentChanged,
                    unit = "%",
                    hint = "Percentual sobre o preço de venda.",
                    error = errors[CalculatorFormField.COMMISSION]
                )
            }

            if (generalError != null) {
                Text(
                    text = generalError.orEmpty(),
                    color = colors.error
                )
            }

            Button(
                onClick = {
                    if (viewModel.calculate()) {
                        onCalculated()
                    } else {

                        // Se houver erros nos dados da primeira
                        // etapa, retorna para mostrá-los.
                        val printingFields = setOf(
                            CalculatorFormField.FILAMENT_WEIGHT,
                            CalculatorFormField.FILAMENT_PRICE,
                            CalculatorFormField.PRINT_HOURS,
                            CalculatorFormField.PRINT_MINUTES,
                            CalculatorFormField.PRINTER
                        )

                        if (
                            viewModel.fieldErrors.value.keys.any {
                                it in printingFields
                            }
                        ) {
                            currentStep = 1
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {

                Text(
                    text = "Calcular preço",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            TextButton(
                onClick = {
                    currentStep = 1
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("← Voltar para impressão")
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}

/**
 * Cabeçalho visual da calculadora.
 */
@Composable
private fun CalculatorHeader() {

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
            .padding(horizontal = 20.dp, vertical = 16.dp),
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
 * Indicador de progresso visual das três etapas.
 */
@Composable
private fun CalculatorStepIndicator(
    currentStep: Int
) {

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

            drawLine(
                color = colors.outline.copy(alpha = 0.5f),
                start = Offset(firstCenter, y),
                end = Offset(lastCenter, y),
                strokeWidth = 2.dp.toPx()
            )

            if (currentStep >= 2) {
                drawLine(
                    color = colors.primary,
                    start = Offset(firstCenter, y),
                    end = Offset(size.width / 2f, y),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            CalculatorStepItem(
                number = 1,
                title = "Impressão",
                selected = currentStep == 1,
                completed = currentStep > 1,
                modifier = Modifier.weight(1f)
            )

            CalculatorStepItem(
                number = 2,
                title = "Custos",
                selected = currentStep == 2,
                completed = currentStep > 2,
                modifier = Modifier.weight(1f)
            )

            CalculatorStepItem(
                number = 3,
                title = "Resultado",
                selected = false,
                completed = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CalculatorStepItem(
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
                color = if (selected) {
                    colors.onPrimary
                } else if (completed) {
                    colors.primary
                } else {
                    colors.onSurfaceVariant
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
            }
        )
    }
}

/**
 * Campo de entrada com unidade e erro por campo.
 */
@Composable
private fun CalculatorTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    unit: String? = null,
    hint: String? = null,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Decimal,
    showTitle: Boolean = true
) {

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        if (showTitle) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = if (showTitle) {
                null
            } else {
                { Text(title) }
            },
            suffix = {
                if (unit != null) {
                    Text(
                        text = unit,
                        color = colors.onSurfaceVariant
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            isError = error != null,
            shape = RoundedCornerShape(10.dp)
        )

        if (error != null) {
            Text(
                text = error,
                color = colors.error,
                style = MaterialTheme.typography.bodySmall
            )
        } else if (hint != null) {
            Text(
                text = hint,
                color = colors.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * Seleção de impressora.
 *
 * A lista é recebida externamente.
 * O cadastro local será implementado posteriormente.
 */
@Composable
private fun PrinterSelector(
    printers: List<Printer>,
    selectedPrinterId: String?,
    error: String?,
    onPrinterSelected: (Printer?) -> Unit,
    onManagePrinters: () -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val colors = MaterialTheme.colorScheme

    val selectedPrinter = printers.firstOrNull {
        it.id == selectedPrinterId
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Impressora",
            fontWeight = FontWeight.SemiBold,
            color = colors.onBackground
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = {
                    expanded = true
                },
                enabled = printers.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(10.dp)
            ) {

                Text(
                    text = selectedPrinter?.name
                        ?: "Selecionar impressora",
                    modifier = Modifier.weight(1f)
                )

                Text("▾")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                printers.forEach { printer ->

                    DropdownMenuItem(
                        text = {
                            Text(printer.name)
                        },
                        onClick = {
                            onPrinterSelected(printer)
                            expanded = false
                        }
                    )
                }
            }
        }


        TextButton(
            onClick = onManagePrinters
        ) {
            Text(
                text = "+ Cadastrar ou gerenciar impressoras",
                color = colors.primary,
                fontWeight = FontWeight.SemiBold
            )
        }


        if (error != null) {
            Text(
                text = error,
                color = colors.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
