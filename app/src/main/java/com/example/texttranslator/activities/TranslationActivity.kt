package com.example.texttranslator.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.texttranslator.activities.ui.theme.TextTranslatorTheme
import com.example.texttranslator.screen.BeforeTranslateCard
import com.example.texttranslator.screen.TranslationCardUI
import com.example.texttranslator.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TranslationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val original = intent.getStringExtra("original_text") ?: ""
        val translated = intent.getStringExtra("translated_text") ?: ""
        val source = intent.getStringExtra("source_lang") ?: ""
        val target = intent.getStringExtra("target_lang") ?: ""
        enableEdgeToEdge()
        setContent {
            TextTranslatorTheme {

                    TranslationScreen (
                        originalText = original,
                        translatedText = translated,
                        sourceLang = source,
                        targetLang = target
                    )
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    originalText: String,
    translatedText: String,
    sourceLang: String,
    targetLang: String
) {
    var original by remember { mutableStateOf(originalText) }
    var showBeforeCard by remember { mutableStateOf(false) }
    var showEditBeforeCard by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Translation") })
        }
    ) { padding ->
        if (showEditBeforeCard) {

            Box(modifier = Modifier.padding(padding)) {
                BeforeTranslateCard(
                    originalText,
                    translatedText,
                    sourceLang,
                    targetLang
                )
                Log.d("lang",sourceLang+"   in ac eDIT $targetLang    $originalText     $translatedText")

            }
        }
        else if(showBeforeCard){
            Box(modifier = Modifier.padding(padding)) {
                BeforeTranslateCard(
                    "",
                    "",
                    sourceLang,
                    targetLang
                )
                Log.d("lang",sourceLang+"   in ac X $targetLang    $originalText     $translatedText")

            }
        }
        else {
            TranslationCardUI(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                originalText = original,
                translatedText = translatedText,
                onOriginalTextChange = { updatedText -> original = updatedText },
                onEditClick = {  showEditBeforeCard = true },
                onClearClick = {
                    showBeforeCard = true
                },
                onCopyOriginal = { /* TODO */ },
                onLineOriginal = { /* TODO */ },
                onSpeakOriginal = { /* TODO */ },
                onCopyTranslated = { /* TODO */ },
                onLineTranslated = { /* TODO */ },
                onCaptureTranslated = { /* TODO */ },
                onBookmarkTranslated = { /* TODO */ }
            )
        }
    }
}

