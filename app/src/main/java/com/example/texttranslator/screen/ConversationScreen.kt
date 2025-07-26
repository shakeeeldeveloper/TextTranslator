package com.example.texttranslator.screen

import android.annotation.SuppressLint
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.texttranslator.R // Replace with your actual package name
import com.example.texttranslator.activities.LanguageActivity
import com.example.texttranslator.viewmodels.ChatViewModel
import com.example.texttranslator.viewmodels.HomeViewModel
import com.example.texttranslator.viewmodels.SpeechViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen() {
    val scrollState = rememberScrollState()
    var isRotated by remember { mutableStateOf(false) }
    val homeViewModel: HomeViewModel = hiltViewModel()

    val chatViewModel: ChatViewModel = hiltViewModel()

    val context = LocalContext.current





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
                        modifier = Modifier
                            .padding(end = 12.dp),
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



                        PersonCard(
                    language = homeViewModel.firstLang,
                    person = "first",
                    text=chatViewModel.firsttext,
                    chatViewModel=chatViewModel,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                        .height(300.dp)
                        .graphicsLayer(rotationZ = 180f), // mimic 180-degree rotation
                        onMicClick = {

                            chatViewModel.currentSpeaker="first"
                        },
                            onSpeak = {
                                Toast.makeText(context,"Speak "+"${chatViewModel.firsttext}", Toast.LENGTH_SHORT).show()

                            },
                            homeViewModel,
                            chatViewModel
                )
                Spacer(modifier = Modifier.height(15.dp))
                PersonCard(
                    language = homeViewModel.secondLang,
                    person = "second",
                    text=chatViewModel.secondtext,
                    chatViewModel=chatViewModel,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .height(300.dp),
                    onMicClick = {
                        chatViewModel.currentSpeaker="second"


                    },
                    onSpeak = {
                        Toast.makeText(context,"Speak "+"${chatViewModel.secondtext}", Toast.LENGTH_SHORT).show()
                    },
                    homeViewModel = homeViewModel,
                    chatViewModel1 = chatViewModel
                )
                Spacer(modifier = Modifier.height(15.dp))


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


@SuppressLint("SuspiciousIndentation")
@Composable
fun PersonCard(

    language: String,
    person: String,
    text: MutableState<String>,
    chatViewModel: ChatViewModel,
    modifier: Modifier = Modifier,
    onMicClick: () -> Unit,
    onSpeak:()-> Unit,
    homeViewModel: HomeViewModel,
    chatViewModel1: ChatViewModel

) {
   // val  homeViewModel: HomeViewModel = hiltViewModel()
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
            Log.d("person",chatViewModel.currentSpeaker+"in trans")

            if (chatViewModel.currentSpeaker == "first") {
                chatViewModel.setTexts(homeViewModel.inputText,translated)

               // chatViewModel.firsttext = translated
            } else {
                chatViewModel.setTexts(translated,homeViewModel.inputText)


               // spokenText = translated
            }


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

                //spokenText = spokenTextFromMic // ✅ set value here
                text.value = spokenTextFromMic .toString()// ✅ set value here
                speechViewModel.updateSpokenText(spokenTextFromMic)
                homeViewModel.inputText = spokenTextFromMic
                lastSpeaker=spokenTextFromMic
                //chatViewModel1.currentSpeaker=person
                Log.d("person",chatViewModel.currentSpeaker+"in mic")

                if(chatViewModel.currentSpeaker.equals("second"))
                {
                   // homeViewModel.swapLanguages()
                    homeViewModel.translate()

                }
                else
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
            if (chatViewModel.currentSpeaker.equals("first")) {
                val intent = speechViewModel.getSpeechIntent(homeViewModel.firstLang)
                speechLauncher.launch(intent)
                lastSpeaker="first"
                //chatViewModel.currentSpeaker="first"
            }
            else{
                val intent = speechViewModel.getSpeechIntent(homeViewModel.secondLang)
                speechLauncher.launch(intent)
                lastSpeaker="second"
                //chatViewModel.currentSpeaker="second"
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


        /*    if (homeViewModel.currentLangType == "first") {
                homeViewModel.setLanguages(language,homeViewModel.secondLang)
            } else if (homeViewModel.currentLangType == "second") {
                homeViewModel.setLanguages(homeViewModel.firstLang,language)

            }*/
            Log.d("cur",language+" ${homeViewModel.currentLangType}     ${homeViewModel.firstLang}      ${homeViewModel.secondLang}")

            homeViewModel.updateSelectedLanguage(language)

        }
    }

    LaunchedEffect(micClick) {
        if (micClick) {
            micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            micClick = false
            onMicClick()
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
                        modifier = Modifier
                            .clickable{
                                onSpeak()
                            },
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))


                Text(
                    text = if (text.value.isNotBlank()) text.value else "Speak text",

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,

                    color = if (text.value.isNotBlank()) Color.Black else Color.Gray
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
