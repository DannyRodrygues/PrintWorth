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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dannyrodrygues.printworth.designsystem.theme.PrintWorthTheme
import org.jetbrains.compose.resources.painterResource
import printworth.shared.generated.resources.Res
import printworth.shared.generated.resources.printworth_icon

private val ScreenBackground = Color(0xFF000000)
private val CardBackground = Color(0xFF0B0B0D)
private val TitleTextColor = Color(0xFFE8EEF0)
private val BodyTextColor = Color(0xFFD7E1E5)
private val SecondaryTextColor = Color(0xFF96A6AE)
private val BrandAccent = Color(0xFF23B7AD)
private val ButtonColor = Color(0xFF0A8F87)

@Composable
@Preview
fun App() {
    var showMessage by remember { mutableStateOf(false) }

    PrintWorthTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground)
                .safeContentPadding()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.printworth_icon),
                    contentDescription = "Ícone PrintWorth",
                    modifier = Modifier
                        .height(82.dp)
                        .fillMaxWidth(0.28f),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = TitleTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Print")
                        }
                        withStyle(
                            SpanStyle(
                                color = BrandAccent,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Worth")
                        }
                    },
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Conheça seus custos.\nValorize suas impressões.",
                    color = BodyTextColor,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Calculadora de Impressão 3D",
                        color = TitleTextColor,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Calcule custos de produção e descubra o preço ideal para suas peças.",
                        color = SecondaryTextColor,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { showMessage = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Iniciar cálculo →",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        if (showMessage) {
            AlertDialog(
                onDismissRequest = { showMessage = false },
                containerColor = Color(0xFF1A1A1D),
                titleContentColor = TitleTextColor,
                textContentColor = BodyTextColor,
                title = {
                    Text("PrintWorth")
                },
                text = {
                    Text("A calculadora será implementada na próxima etapa.")
                },
                confirmButton = {
                    TextButton(
                        onClick = { showMessage = false }
                    ) {
                        Text(
                            text = "Entendi",
                            color = BrandAccent
                        )
                    }
                }
            )
        }
    }
}