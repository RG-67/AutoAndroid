package com.project.autoandroid.voice

import android.app.ComponentCaller
import android.content.Intent
import android.graphics.Paint.Align
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.project.autoandroid.R
import com.project.autoandroid.ui.theme.AutoAndroidTheme


class VoiceActivity : ComponentActivity() {

    private var voiceInput by mutableStateOf("")

    private val speechLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    ?.firstOrNull()
                voiceInput = text ?: ""
                if (voiceInput.lowercase().contains("open whatsapp")) {
                    openWhatsApp()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoAndroidTheme {
                VoiceUi(Modifier)
            }
        }
    }


    @Composable
    fun VoiceUi(modifier: Modifier = Modifier) {
        Column(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape)
                    .clickable {
                        speechToText()
                    },
                contentDescription = getString(R.string.app_name),
                painter = painterResource(R.drawable.mic)
            )

            Text(
                modifier = Modifier.padding(10.dp),
                text = voiceInput,
                style = TextStyle(
                    fontSize = TextUnit.Unspecified,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                )
            )
        }

    }


    private fun speechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }
        speechLauncher.launch(intent)
    }


    private fun openWhatsApp() {
        var intent = packageManager.getLaunchIntentForPackage("com.whatsapp")
        if (intent != null) {
            startActivity(intent)
        } else {
            intent = packageManager.getLaunchIntentForPackage("com.whatsapp.w4b")
            if (intent != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }


}