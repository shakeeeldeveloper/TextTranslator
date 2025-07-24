package com.example.texttranslator.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.texttranslator.R
import android.R.attr.text
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.texttranslator.viewmodels.HomeViewModel
import androidx.compose.runtime.collectAsState

import com.example.texttranslator.activities.LanguageActivity
import com.example.texttranslator.activities.TranslationActivity
import com.example.texttranslator.activities.TranslationScreen
import com.example.texttranslator.viewmodels.SpeechViewModel
import java.util.Locale
import android.Manifest
import android.util.Log


@Composable
fun BeforeTranslateCard(

    originalText: String,
    translatedText: String,
    sourceLang: String,
    targetLang: String
) {
  val  homeViewModel: HomeViewModel = hiltViewModel()
  val  speechViewModel: SpeechViewModel = hiltViewModel()


    LaunchedEffect(Unit) {
        homeViewModel.inputText = originalText
        speechViewModel.updateSpokenText(originalText)
    }

 /*   homeViewModel.inputText=originalText
    speechViewModel.updateSpokenText(originalText)*/
    var isText by remember { mutableStateOf(true) }
    var crossClick by remember { mutableStateOf(false) }
    var micClick by remember { mutableStateOf(false) }
    var transBtnClick by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as Activity


Log.d("lang",sourceLang+"   $targetLang")

    var firstLang = sourceLang
    var secondLang = targetLang

    LaunchedEffect(Unit) {
        homeViewModel.setLanguages(sourceLang, targetLang)
    }

   // homeViewModel.setLanguages(sourceLang,targetLang)

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val spokenText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (spokenText != null) {
                speechViewModel.updateSpokenText(spokenText)
                homeViewModel.inputText=spokenText
                isText=true
            }
        }
    }
    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = speechViewModel.getSpeechIntent(homeViewModel.firstLang)
            speechLauncher.launch(intent)
        } else {
            Toast.makeText(context, "Microphone permission is required", Toast.LENGTH_SHORT).show()
        }
    }


/*    var spokenText by remember { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            startSpeechRecognition(context) { result ->
                spokenText = result
            }
        } else {
            Toast.makeText(context, "Microphone permission required", Toast.LENGTH_SHORT).show()
        }
    }*/

    val languageActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val language = data?.getStringExtra("selected_language") ?: ""
            Log.d("cur",language+" ${homeViewModel.currentLangType}")


            if (homeViewModel.currentLangType == "first") {
              homeViewModel.setLanguages(language,homeViewModel.secondLang)
            } else if (homeViewModel.currentLangType == "second") {
               // Log.d("cur",language+" true "+currentLangType)
                homeViewModel.setLanguages(homeViewModel.firstLang,language)


            }
            Log.d("cur",language+" ${homeViewModel.currentLangType}     ${homeViewModel.firstLang}      ${homeViewModel.secondLang}")

              homeViewModel.updateSelectedLanguage(language)

        }
    }

    LaunchedEffect(crossClick) {
        if (crossClick) {
            homeViewModel.inputText = ""
            homeViewModel.translatedText = ""
            speechViewModel.updateSpokenText("")
            isText = false
            crossClick = false

        }
    }

    LaunchedEffect(micClick) {
        if (micClick) {
            micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            micClick = false
        }
    }

    val translated by homeViewModel.translated.collectAsState()

    LaunchedEffect(translated) {
        if (translated.isNotEmpty()) {
            val intent = Intent(context, TranslationActivity::class.java).apply {
                putExtra("original_text", homeViewModel.inputText)
                putExtra("translated_text", translated)
                putExtra("source_lang", homeViewModel.firstLang)
                putExtra("target_lang", homeViewModel.secondLang)
            }
            context.startActivity(intent)
            (context as? Activity)?.finish()
            homeViewModel.clearTranslation()
        }
    }


    Log.d("lang",homeViewModel.firstLang+" home   ${homeViewModel.secondLang}")

   /*  firstLang = homeViewModel.firstLang
     secondLang = homeViewModel.secondLang*/




    Column (
        modifier = Modifier
            .fillMaxSize()

    ){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(73.dp)
            .padding(top = 15.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val context = LocalContext.current

            LanguageButton(homeViewModel.firstLang) {
                homeViewModel.currentLangType="first"
                val intent = Intent(context, LanguageActivity::class.java)
                languageActivityLauncher.launch(intent)
            }

            Icon(
                painter = painterResource(id = R.drawable.rotate_icon),
                contentDescription = "Switch Language",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        Log.d("cur","swipe")

                        // Swap logic
                        homeViewModel.swapLanguages() // optional: if you need external handling
                    }
            )

            LanguageButton(homeViewModel.secondLang) {
                homeViewModel.currentLangType="second"
                Log.d("cur",homeViewModel.currentLangType)
                val intent = Intent(context, LanguageActivity::class.java)
                languageActivityLauncher.launch(intent)
            }
        }

    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(329.dp)
            .padding( 15.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sourceLang,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.cross_icon),
                        contentDescription = "Clear Text",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                crossClick=true
                                }
                    )
                   /* if (crossClick){
                        homeViewModel.inputText =""
                        isText = false
                        speechViewModel.updateSpokenText("")
                    }*/
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = homeViewModel.inputText,
                    onValueChange = { newText ->
                        homeViewModel.inputText = newText   // Update the view model
                        isText = newText.isNotEmpty()
                        speechViewModel.updateSpokenText(newText) // Optional: Keep in sync
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    placeholder = { Text("Type your text here") },
                    maxLines = 5,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,

                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        disabledIndicatorColor = Color.White,
                        cursorColor = Color.Black
                    )
                )


            }

            Image(
                painter = painterResource(
                    id = if (isText) R.drawable.trans_svg else R.drawable.voice_icon
                ),
                contentDescription = "Voice Input",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp)
                    .clickable {
                        if (homeViewModel.inputText.isBlank()) {
                          //  launcher.launch(Manifest.permission.RECORD_AUDIO)

                            micClick = true
                        } else {
                            transBtnClick = true
                        }
                    }

            )
            if (transBtnClick){
                homeViewModel.inputText = speechViewModel.spokenText // assign inputText before translate

                homeViewModel.translateText()
                transBtnClick=false




            }

            /*if (micClick) {
                startSpeechRecognition(firstLang) // 👈 invoke here

            }*/

        }
    }
    }


}
/*
fun startSpeechRecognition(context: Context, onResult: (String) -> Unit) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...")
    }

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    recognizer.setRecognitionListener(object : RecognitionListener {
        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                onResult(matches[0])
            }
        }

        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer is busy"
                SpeechRecognizer.ERROR_SERVER -> "Server error"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                else -> "Unknown error code: $error"
            }
            Toast.makeText(context, "Speech error: $message", Toast.LENGTH_LONG).show()
            Log.e("SpeechRecognizer", "Error code: $error ($message)")
        }



        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    recognizer.startListening(intent)
}
*/



/*@Preview(showBackground = true)
@Composable
fun BeforeTranslateCardPreview() {
    BeforeTranslateCard(
        originalText = "Hello, how are you?",
        translatedText = "سلام، آپ کیسے ہیں؟",
        sourceLang = "English",
        targetLang = "Urdu"
    )
}*/


