
package com.dannyrodrygues.printworth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

import com.dannyrodrygues.printworth.data.repository.AndroidPrinterRepository

class MainActivity : ComponentActivity() {

    private val printerRepository by lazy {
        AndroidPrinterRepository(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                printerRepository = printerRepository
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val context = LocalContext.current

    val repository = remember(context) {
        AndroidPrinterRepository(context.applicationContext)
    }

    App(
        printerRepository = repository
    )
}
