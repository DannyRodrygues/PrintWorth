
package com.dannyrodrygues.printworth.presentation.printer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dannyrodrygues.printworth.domain.catalog.PrinterCatalog
import com.dannyrodrygues.printworth.domain.model.Printer
import org.jetbrains.compose.resources.painterResource
import printworth.shared.generated.resources.Res
import printworth.shared.generated.resources.printworth_icon

/**
 * Gerencia configurações pessoais de impressoras.
 *
 * O catálogo pré-cadastrado é fixo e funciona offline.
 * Os cadastros pessoais são salvos pelo ViewModel.
 */
@Composable
fun PrinterManagementScreen(
    viewModel: PrinterManagementViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val savedPrinters by viewModel.printers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val error by viewModel.error.collectAsState()
    val message by viewModel.message.collectAsState()

    var registrationMode by rememberSaveable {
        mutableStateOf("catalog")
    }

    var selectedBrand by rememberSaveable {
        mutableStateOf("Bambu Lab")
    }

    var selectedModelId by rememberSaveable {
        mutableStateOf("bambu-a1")
    }

    var printerName by rememberSaveable {
        mutableStateOf("Bambu Lab A1")
    }

    var powerText by rememberSaveable {
        mutableStateOf("95")
    }

    var attemptedSave by rememberSaveable {
        mutableStateOf(false)
    }

    var editingPrinter by remember {
        mutableStateOf<Printer?>(null)
    }

    var deletingPrinter by remember {
        mutableStateOf<Printer?>(null)
    }

    var editName by rememberSaveable {
        mutableStateOf("")
    }

    var editPower by rememberSaveable {
        mutableStateOf("")
    }

    val selectedModel =
        PrinterCatalog.findById(selectedModelId)

    val nameIsValid =
        printerName.trim().isNotEmpty() &&
                printerName.trim().length <= 80

    val enteredPower = powerText.trim().toIntOrNull()

    val powerIsValid =
        enteredPower != null && enteredPower > 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .safeContentPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF103D46),
                            Color(0xFF07515A)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Image(
                painter = painterResource(
                    Res.drawable.printworth_icon
                ),
                contentDescription = "Ícone PrintWorth",
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "PrintWorth",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(onClick = onBack) {
            Text("← Voltar")
        }

        Text(
            text = "Minhas impressoras",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        Text(
            text = "Escolha um modelo do catálogo ou " +
                    "cadastre sua própria impressora.",
            color = colors.onSurfaceVariant
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = colors.surface
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "Cadastrar impressora",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val modes = listOf(
                        "catalog" to "Do catálogo",
                        "custom" to "Personalizada"
                    )

                    modes.forEachIndexed { index, item ->
                        SegmentedButton(
                            selected = registrationMode == item.first,
                            onClick = {
                                registrationMode = item.first
                                attemptedSave = false

                                if (item.first == "custom") {
                                    printerName = ""
                                    powerText = ""
                                } else {
                                    val model =
                                        PrinterCatalog.findById(
                                            selectedModelId
                                        )

                                    printerName =
                                        model?.displayName.orEmpty()

                                    powerText = model
                                        ?.suggestedAveragePowerWatts
                                        ?.toString()
                                        .orEmpty()
                                }
                            },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = modes.size
                            ),
                            label = {
                                Text(item.second)
                            }
                        )
                    }
                }

                if (registrationMode == "catalog") {

                    PrinterDropdown(
                        label = "Fabricante",
                        selectedText = selectedBrand,
                        options = PrinterCatalog.brands()
                            .map { it to it },
                        onSelected = { brand ->
                            selectedBrand = brand

                            val firstModel =
                                PrinterCatalog
                                    .modelsByBrand(brand)
                                    .firstOrNull()

                            if (firstModel != null) {
                                selectedModelId = firstModel.id
                                printerName = firstModel.displayName

                                powerText = firstModel
                                    .suggestedAveragePowerWatts
                                    ?.toString()
                                    .orEmpty()
                            }

                            attemptedSave = false
                        }
                    )

                    PrinterDropdown(
                        label = "Modelo",
                        selectedText = selectedModel?.model
                            ?: "Selecione um modelo",
                        options = PrinterCatalog
                            .modelsByBrand(selectedBrand)
                            .map { it.id to it.model },
                        onSelected = { modelId ->
                            selectedModelId = modelId

                            val model =
                                PrinterCatalog.findById(modelId)

                            printerName =
                                model?.displayName.orEmpty()

                            powerText = model
                                ?.suggestedAveragePowerWatts
                                ?.toString()
                                .orEmpty()

                            attemptedSave = false
                        }
                    )

                    if (selectedModel != null) {

                        if (
                            selectedModel.maxPowerWatts != null &&
                            selectedModel.maxPowerAtVoltageVolts != null
                        ) {
                            Text(
                                text = "Potência máxima declarada: " +
                                        "${selectedModel.maxPowerWatts} W " +
                                        "em " +
                                        "${selectedModel.maxPowerAtVoltageVolts} V.",
                                color = colors.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            text = if (
                                selectedModel
                                    .suggestedAveragePowerWatts != null
                            ) {
                                "Potência média sugerida: " +
                                        "${selectedModel.suggestedAveragePowerWatts} W. " +
                                        "Confira o consumo real da sua impressora."
                            } else {
                                "Este modelo não possui uma estimativa " +
                                        "de consumo médio pré-definida. " +
                                        "Informe um valor para sua impressora."
                            },
                            color = colors.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                OutlinedTextField(
                    value = printerName,
                    onValueChange = {
                        printerName = it
                        viewModel.clearMessages()
                    },
                    label = {
                        Text("Nome da minha impressora")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = attemptedSave && !nameIsValid
                )

                if (attemptedSave && !nameIsValid) {
                    Text(
                        text = "Informe um nome com até 80 caracteres.",
                        color = colors.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = powerText,
                    onValueChange = {
                        powerText = it
                        viewModel.clearMessages()
                    },
                    label = {
                        Text("Consumo médio estimado (W)")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = attemptedSave && !powerIsValid
                )

                if (attemptedSave && !powerIsValid) {
                    Text(
                        text = "Informe uma potência inteira " +
                                "maior que zero.",
                        color = colors.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    text = "Este valor será usado para calcular o " +
                            "custo de energia. Você poderá ajustá-lo " +
                            "na calculadora.",
                    color = colors.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )

                Button(
                    onClick = {
                        attemptedSave = true

                        if (nameIsValid && powerIsValid) {
                            viewModel.addPrinter(
                                name = printerName.trim(),
                                averagePowerWatts =
                                    requireNotNull(enteredPower)
                            )
                        }
                    },
                    enabled = !isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (isSaving) {
                            "Salvando..."
                        } else {
                            "Cadastrar minha impressora"
                        }
                    )
                }
            }
        }

        if (error != null) {
            Text(
                text = error.orEmpty(),
                color = colors.error
            )
        }

        if (message != null) {
            Text(
                text = message.orEmpty(),
                color = colors.primary
            )
        }

        HorizontalDivider()

        Text(
            text = "Impressoras cadastradas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )

        if (isLoading) {

            CircularProgressIndicator()

        } else if (savedPrinters.isEmpty()) {

            Text(
                text = "Você ainda não cadastrou " +
                        "nenhuma impressora pessoal.",
                color = colors.onSurfaceVariant
            )

        } else {

            savedPrinters.forEach { printer ->

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = colors.surface
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        Text(
                            text = printer.name,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )

                        Text(
                            text = "Consumo médio: " +
                                    "${printer.averagePowerWatts} W",
                            color = colors.onSurfaceVariant
                        )

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            OutlinedButton(
                                onClick = {
                                    editingPrinter = printer
                                    editName = printer.name
                                    editPower = printer
                                        .averagePowerWatts
                                        .toString()
                                },
                                enabled = !isSaving,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Editar")
                            }

                            TextButton(
                                onClick = {
                                    deletingPrinter = printer
                                },
                                enabled = !isSaving,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Excluir")
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Os modelos do catálogo permanecem " +
                    "disponíveis mesmo que você exclua " +
                    "uma impressora pessoal.",
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant
        )
    }

    // Diálogo de edição de impressora pessoal.
    if (editingPrinter != null) {

        val printer = requireNotNull(editingPrinter)
        val power = editPower.trim().toIntOrNull()

        val canSave =
            editName.trim().isNotEmpty() &&
                    editName.trim().length <= 80 &&
                    power != null &&
                    power > 0

        AlertDialog(
            onDismissRequest = {
                editingPrinter = null
            },
            title = {
                Text("Editar impressora")
            },
            text = {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = editName,
                        onValueChange = {
                            editName = it
                        },
                        label = {
                            Text("Nome")
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editPower,
                        onValueChange = {
                            editPower = it
                        },
                        label = {
                            Text("Consumo médio (W)")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = canSave && !isSaving,
                    onClick = {
                        viewModel.updatePrinter(
                            printer.copy(
                                name = editName.trim(),
                                averagePowerWatts =
                                    requireNotNull(power)
                            )
                        )
                        editingPrinter = null
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        editingPrinter = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Confirmação obrigatória antes de excluir.
    if (deletingPrinter != null) {

        val printer = requireNotNull(deletingPrinter)

        AlertDialog(
            onDismissRequest = {
                deletingPrinter = null
            },
            title = {
                Text("Excluir impressora?")
            },
            text = {
                Text(
                    "Deseja excluir '${printer.name}' " +
                            "das suas impressoras cadastradas?"
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !isSaving,
                    onClick = {
                        viewModel.deletePrinter(printer.id)
                        deletingPrinter = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deletingPrinter = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Seletor reutilizável de fabricante ou modelo.
 */
@Composable
private fun PrinterDropdown(
    label: String,
    selectedText: String,
    options: List<Pair<String, String>>,
    onSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = {
                    expanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedText,
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
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(option.second)
                        },
                        onClick = {
                            onSelected(option.first)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
