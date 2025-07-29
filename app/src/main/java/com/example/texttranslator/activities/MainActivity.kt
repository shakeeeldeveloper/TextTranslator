package com.example.texttranslator.activities

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.texttranslator.screen.HomeScreen
import com.example.texttranslator.screen.LottieTranslateAnimation
import com.example.texttranslator.ui.theme.TextTranslatorTheme
import com.example.texttranslator.viewmodels.HomeViewModel
import com.example.texttranslator.viewmodels.SpeechViewModel
import com.example.texttranslator.R
import com.example.texttranslator.screen.ConversationScreen
import com.example.texttranslator.screen.ConversationScreenWithXMLFragment
import com.example.texttranslator.screen.HistoryScreen
import com.example.texttranslator.screen.SettingScreen
import com.example.texttranslator.viewmodel.MainViewModel
import com.example.texttranslator.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var speechViewModel: SpeechViewModel
  //  private val mainViewModel: MainViewModel by viewModels()
  private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speechViewModel = ViewModelProvider(this)[SpeechViewModel::class.java]
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            TextTranslatorTheme {
                var isAnimationDone by remember { mutableStateOf(false) }

                if (isAnimationDone) {
                    OnlyHomeScreen(
                        startSpeechToText = { selectedLang ->
                            startSpeechRecognition(selectedLang)
                        },
                        mainViewModel = mainViewModel // pass to Composable

                    )
                } else {
                    LottieTranslateAnimation(
                        onNavigateToAdScreen = {
                            isAnimationDone = true
                        }
                    )
                }
            }
        }
     /*   // Handle system back button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mainViewModel.isOnHomeScreen()) {
                    moveTaskToBack(true) // Exit app to home screen
                } else {
                    mainViewModel.setScreen("Home") // Go to home screen
                }
            }
        })*/


    }

    override fun onBackPressed() {
       // super.onBackPressed()
        Log.d("selectedScreen","${mainViewModel.selectedScreen.value}")

        if (!mainViewModel.showHistory.value) {
            if (mainViewModel.isOnHomeScreen()) {
                moveTaskToBack(true) // Exit app to home screen
                super.onBackPressed()
            } else {
                mainViewModel.setScreen("Home") // Go to home screen
                mainViewModel.setHistory(false)
            }
        }
        else{
            if (mainViewModel.isOnHomeScreen()) {
                moveTaskToBack(true) // Exit app to home screen
                super.onBackPressed()
            } else {
                mainViewModel.setScreen(mainViewModel.selectedScreen.value) // Go to home screen
                mainViewModel.setHistory(false)
            }
        }

    }

    private val speechLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val spokenText =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                if (spokenText != null) {
                    speechViewModel.updateSpokenText(spokenText)
                }
            }
        }

    private fun startSpeechRecognition(selectedLang: String) {
        val intent = speechViewModel.getSpeechIntent(selectedLang)
        speechLauncher.launch(intent)
    }
}


@Composable
fun OnlyHomeScreen(
    startSpeechToText: (String) -> Unit,
    mainViewModel: MainViewModel // <-- injected from Activity

) {

    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val speechViewModel: SpeechViewModel = hiltViewModel()
    val spokenText = speechViewModel.spokenText

    var isSwitchChecked by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("Home") }

    var showHistoryScreen by remember { mutableStateOf(false) }
    val forHistoryViewModel: HomeViewModel = hiltViewModel()
    val chatViewModel: ChatViewModel = hiltViewModel()

    forHistoryViewModel.loadHistory()



    Scaffold(
        bottomBar = {
            if (!mainViewModel.showHistory.value) { // (!showHistoryScreen)// hide nav bar during history
                BottomNavigationBar(
                    selectedScreen = mainViewModel.selectedScreen.value,
                    onItemSelected = { mainViewModel.setScreen(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {

                mainViewModel.showHistory.value -> {
                    // Show HistoryScreen overlay
                    HistoryScreen(
                        historyItems = forHistoryViewModel.historyList.value,
                        onBack = {  mainViewModel.setHistory(false) },
                        onClear = { forHistoryViewModel.clearHistory() },
                        onDelete = { forHistoryViewModel.deleteItem(it) }
                    )

                }

                mainViewModel.selectedScreen.value == "Home" -> {
                    chatViewModel.firsttext.value=""
                    chatViewModel.secondtext.value=""

                    HomeScreen(
                        onSettingsClick = { /* no-op */ },
                        onSwitchToggle = { isSwitchChecked = it },
                        isSwitchChecked = isSwitchChecked,
                        onTransBtnClick = {
                            Log.d("sp_text", speechViewModel.spokenText)
                            viewModel.inputText = speechViewModel.spokenText
                            viewModel.translateTextHome()
                        },
                        onVoiceInputClick = {
                            startSpeechToText(viewModel.firstLang)
                        },
                        onLangSwitch = { },
                        textInput = spokenText,
                        onTextChange = { speechViewModel.updateSpokenText(it) },
                        onLangSelected = { selectedLang ->
                            val intent = Intent(context, LanguageActivity::class.java)
                            intent.putExtra("selected_language", selectedLang)
                            context.startActivity(intent)
                        },
                        translatedText = viewModel.translatedText,
                        isTranslating = viewModel.isTranslating,
                        errorMessage = viewModel.errorMessage
                    )
                }

                mainViewModel.selectedScreen.value == "Chat" ->  key("Setting"){
                    chatViewModel.firsttext.value=""
                    chatViewModel.secondtext.value=""
                    ConversationScreen(
                        onOpenHistory = {
                            showHistoryScreen = true
                            mainViewModel.setHistory(true)

                        }
                    )
                }

                mainViewModel.selectedScreen.value == "Setting" -> key("Setting") {
                    chatViewModel.firsttext.value=""
                    chatViewModel.secondtext.value=""
                    SettingScreen(
                        onOpenHistory = {
                            showHistoryScreen = true
                            mainViewModel.setHistory(true)

                        }
                    )
                }
            }
        }
    }
}


/*@Composable
fun OnlyHomeScreen(
    startSpeechToText: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val speechViewModel: SpeechViewModel = hiltViewModel()
    val spokenText = speechViewModel.spokenText

    var isSwitchChecked by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("Home") }

    var showHistoryScreen by remember { mutableStateOf(false) }


    Scaffold(
        bottomBar = {

            BottomNavigationBar(
                selectedScreen = selectedScreen,
                onItemSelected = { selectedScreen = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (selectedScreen) {



                "Home" -> HomeScreen(
                    onSettingsClick = {
                    },
                    onSwitchToggle = { isSwitchChecked = it },
                    isSwitchChecked = isSwitchChecked,
                    onTransBtnClick = {
                        Log.d("sp_text", speechViewModel.spokenText)
                        viewModel.inputText = speechViewModel.spokenText
                        viewModel.translateTextHome()
                    },
                    onVoiceInputClick = {
                        startSpeechToText(viewModel.firstLang)
                    },
                    onLangSwitch = { *//* Optional *//* },
                    textInput = spokenText,
                    onTextChange = { speechViewModel.updateSpokenText(it) },
                    onLangSelected = { selectedLang ->
                        val intent = Intent(context, LanguageActivity::class.java)
                        intent.putExtra("selected_language", selectedLang)
                        context.startActivity(intent)
                    },
                    translatedText = viewModel.translatedText,
                    isTranslating = viewModel.isTranslating,
                    errorMessage = viewModel.errorMessage
                )

                "Chat" -> ConversationScreen( )
                "Setting" -> SettingScreen()
            }

        }
    }
}*/


sealed class Screen {
    object Conversation : Screen()
    object Setting : Screen()
    data class History(val from: Screen) : Screen()
}

data class NavItem(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

val navItems = listOf(
    NavItem("Home", R.drawable.home_icon, R.drawable.home_icon_outline),
    NavItem("Chat", R.drawable.chat_icon_filled, R.drawable.chat_icon_outline),
    NavItem("Setting", R.drawable.setting_icon_filled, R.drawable.setting_icon)
)

@Composable
fun BottomNavigationBar(
    selectedScreen: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar {
        navItems.forEach { item ->
            val isSelected = selectedScreen == item.label
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.label) },
                icon = {
                    Icon(
                        painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = item.label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                //label = { Text(item.label) }
            )
        }
    }
}


/*@Composable
fun ChatScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Chat Screen")
    }
}

@Composable
fun CameraScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Camera Screen")
    }
}*/



