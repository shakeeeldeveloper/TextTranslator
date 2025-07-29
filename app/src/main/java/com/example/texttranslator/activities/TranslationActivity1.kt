package com.example.texttranslator.activities

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.texttranslator.screen.BeforeTranslateCard
import com.example.texttranslator.screen.TranslationCardUI
import com.example.texttranslator.viewmodels.HomeViewModel


import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.texttranslator.activities.ui.theme.TextTranslatorTheme
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder

@AndroidEntryPoint
class TranslationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get intent extras
        val original = intent.getStringExtra("original_text") ?: ""
        val translated = intent.getStringExtra("translated_text") ?: ""
        val source = intent.getStringExtra("source_lang") ?: ""
        val target = intent.getStringExtra("target_lang") ?: ""

        enableEdgeToEdge()

        setContent {
            TextTranslatorTheme {
                TranslationNavGraph(
                    originalText = original,
                    translatedText = translated,
                    sourceLang = source,
                    targetLang = target
                )
            }
        }
    }
}

@Composable
fun TranslationNavGraph(
    originalText: String,
    translatedText: String,
    sourceLang: String,
    targetLang: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "translation"
    ) {
        composable("translation") {
            TranslationScreen(
                originalText = originalText,
                translatedText = translatedText,
                sourceLang = sourceLang,
                targetLang = targetLang,
                navController = navController
            )
        }

        // If you want to add more screens, define routes like:
        // composable("details/{id}") { backStackEntry -> ... }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    originalText: String,
    translatedText: String,
    sourceLang: String,
    targetLang: String,
    navController: androidx.navigation.NavController
) {
    var original by remember { mutableStateOf(originalText) }
    var showBeforeCard by remember { mutableStateOf(false) }
    var showEditBeforeCard by remember { mutableStateOf(false) }

    val viewModel: HomeViewModel = hiltViewModel()
    viewModel.inputText = originalText
    viewModel.translatedText = translatedText
    viewModel.setLanguages(sourceLang, targetLang)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Translation") })
        }
    ) { padding ->
        if (showEditBeforeCard) {
            Box(modifier = Modifier.padding(padding)) {
                BeforeTranslateCard(originalText, translatedText, sourceLang, targetLang)
            }
        } else if (showBeforeCard) {
            Box(modifier = Modifier.padding(padding)) {
                BeforeTranslateCard("", "", sourceLang, targetLang)
            }
        } else {
            TranslationCardUI(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                originalText = original,
                translatedText = translatedText,
                onOriginalTextChange = { updatedText -> original = updatedText },
                onEditClick = { showEditBeforeCard = true },
                onClearClick = { showBeforeCard = true },
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

