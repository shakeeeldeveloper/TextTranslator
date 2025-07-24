package com.example.texttranslator.screen

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.texttranslator.R // Replace with your actual package name
import com.example.texttranslator.activities.LanguageActivity
import com.example.texttranslator.activities.TranslationActivity
import com.example.texttranslator.viewmodels.HomeViewModel
import com.example.texttranslator.viewmodels.SpeechViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen() {
    val scrollState = rememberScrollState()
    var isRotated by remember { mutableStateOf(false) }
    val homeViewModel: HomeViewModel = hiltViewModel()







    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
    ) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Conversation",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.con_icon),
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            modifier = Modifier.shadow(4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(rotationZ = if (isRotated) 180f else 0f)
        ) {

            Column {


                /*PersonCard(
                    spokenText = firstPersonText,
                    onSpokenTextUpdate = { firstPersonText = it },
                    translatedText = secondPersonText,
                    onTranslatedTextUpdate = { secondPersonText = it },
                    language = homeViewModel.firstLang,
                    person = "first",
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                        .height(300.dp)
                        .graphicsLayer(rotationZ = 180f)
                )

                Spacer(modifier = Modifier.height(15.dp))

                PersonCard(
                    spokenText = secondPersonText,
                    onSpokenTextUpdate = { secondPersonText = it },
                    translatedText = firstPersonText,
                    onTranslatedTextUpdate = { firstPersonText = it },
                    language = homeViewModel.secondLang,
                    person = "second",
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .height(300.dp)
                )*/

                        PersonCard(
                    language = homeViewModel.firstLang,
                    person = "first",
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                        .height(300.dp)
                        .graphicsLayer(rotationZ = 180f) // mimic 180-degree rotation
                )
                Spacer(modifier = Modifier.height(15.dp))
                PersonCard(
                    language = homeViewModel.secondLang,
                    person = "second",
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .height(300.dp)
                )

            }

            Image(
                painter = painterResource(id = R.drawable.rotate_circle_bg),
                contentDescription = "Center Icon",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 292.dp)
                    .clickable {
                        isRotated = !isRotated
                    }
            )
        }
    }
}


@Composable
fun PersonCard(

    language: String,
    person: String,
    modifier: Modifier = Modifier
) {
    val  homeViewModel: HomeViewModel = hiltViewModel()
    val  speechViewModel: SpeechViewModel = hiltViewModel()

    var micClick by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }

    var spokenText by remember { mutableStateOf("") } // Holds displayed text
    var lastSpeaker by remember { mutableStateOf("first") } // or "second"
    var firstTranslated by remember { mutableStateOf("") }
    var secondTranslated by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as Activity

    LaunchedEffect(Unit) {
        homeViewModel.setLanguages(homeViewModel.firstLang, homeViewModel.secondLang)
    }
    val translated by homeViewModel.translated.collectAsState()

    LaunchedEffect(translated) {
        if (translated.isNotEmpty()) {
           /* val intent = Intent(context, TranslationActivity::class.java).apply {
                putExtra("original_text", homeViewModel.inputText)
                putExtra("translated_text", translated)
                putExtra("source_lang", homeViewModel.firstLang)
                putExtra("target_lang", homeViewModel.secondLang)
            }
            context.startActivity(intent)
            (context as? Activity)?.finish()*/
            if (lastSpeaker == "first") {
                spokenText = translated
            } else {
                spokenText = translated
            }

       /*     Log.d("trans", "$spokenText\n " +
                    "$translated\n " +
                    "${homeViewModel.inputText}\n" +
                    " ${homeViewModel.firstLang}\n " +
                    "${homeViewModel.secondLang}\n")*/
            Toast.makeText(context,"Translated "+"$spokenText\n " +
                    "$translated\n " +
                    "${homeViewModel.inputText}\n" +
                    " ${homeViewModel.firstLang}\n " +
                    "${homeViewModel.secondLang}\n", Toast.LENGTH_SHORT).show()

            homeViewModel.clearTranslation()
        }
    }


    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val spokenTextFromMic = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (spokenTextFromMic != null) {
                spokenText = spokenTextFromMic // ✅ set value here
                speechViewModel.updateSpokenText(spokenTextFromMic)
                homeViewModel.inputText = spokenTextFromMic
                lastSpeaker=spokenTextFromMic



                /*Log.d("ttttttttt", homeViewModel.inputText
                        +"${homeViewModel.firstLang}"
                        +"${homeViewModel.secondLang}")*/


                homeViewModel.translateText()

               /* Toast.makeText(context,homeViewModel.inputText
                        +"${homeViewModel.firstLang}"
                        +"${homeViewModel.secondLang}", Toast.LENGTH_SHORT).show()*/
            }
            else {
                Log.d(
                    "ttttttttt",
                    homeViewModel.inputText + "${homeViewModel.firstLang}" + "${homeViewModel.secondLang}"
                )
                Toast.makeText(context,"nulllll "+homeViewModel.inputText
                        +"${homeViewModel.firstLang}"
                        +"${homeViewModel.secondLang}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (person.equals("first")) {
                val intent = speechViewModel.getSpeechIntent(homeViewModel.firstLang)
                speechLauncher.launch(intent)
                lastSpeaker="first"
            }
            else{
                val intent = speechViewModel.getSpeechIntent(homeViewModel.secondLang)
                speechLauncher.launch(intent)
                lastSpeaker="second"
            }
        } else {
            Toast.makeText(context, "Microphone permission is required", Toast.LENGTH_SHORT).show()
        }
    }

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

    LaunchedEffect(micClick) {
        if (micClick) {
            micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            micClick = false
        }
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.End)
                        .background(
                            color = Color(0xFFE7F3FF),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.speak_blue),
                        contentDescription = "Speak",
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))


                Text(
                    text = if (spokenText.isNotBlank()) spokenText else "Speak text",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = if (spokenText.isNotBlank()) Color.Black else Color.Gray
                )


                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.size(width = 85.dp, height = 48.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7F3FF))
                    ) {
                        LanguageButton(language) {
                            homeViewModel.currentLangType=person
                            val intent = Intent(context, LanguageActivity::class.java)
                            languageActivityLauncher.launch(intent)
                        }

                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        micClick=true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.voice_icon1),
                            contentDescription = "Mic",
                            modifier = Modifier.size(40.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}
