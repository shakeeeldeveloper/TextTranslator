package com.example.texttranslator.activities

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.texttranslator.screen.SettingScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var speechViewModel: SpeechViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speechViewModel = ViewModelProvider(this)[SpeechViewModel::class.java]
        setContent {
            TextTranslatorTheme {
                var isAnimationDone by remember { mutableStateOf(false) }

                if (isAnimationDone) {
                    OnlyHomeScreen(
                        startSpeechToText = { selectedLang ->
                            startSpeechRecognition(selectedLang)
                        }
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

        /*setContent {
            TextTranslatorTheme {
                OnlyHomeScreen(
                    startSpeechToText = { selectedLang ->
                        startSpeechRecognition(selectedLang)
                    }
                )
            }
        }*/
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


//without buttom navigation

/*@Composable
fun OnlyHomeScreen(
    startSpeechToText: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val speechViewModel: SpeechViewModel = hiltViewModel()
    val spokenText = speechViewModel.spokenText

    var isSwitchChecked by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            HomeScreen(
                onSettingsClick = { */
/* Optional: Handle settings */
/* },
                onSwitchToggle = { isSwitchChecked = it },
                isSwitchChecked = isSwitchChecked,
                onTransBtnClick = {
                    Log.d("sp_text", speechViewModel.spokenText)
                    viewModel.inputText = speechViewModel.spokenText
                    viewModel.translateText()
                },
                onVoiceInputClick = {
                    startSpeechToText(viewModel.firstLang)
                },
                onLangSwitch = { *//* Optional: Handle lang switch *//* },
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
    }
}*/



@Composable
fun OnlyHomeScreen(
    startSpeechToText: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = hiltViewModel()
    val speechViewModel: SpeechViewModel = hiltViewModel()
    val spokenText = speechViewModel.spokenText

    var isSwitchChecked by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("Home") }

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
                    onLangSwitch = { /* Optional */ },
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

                "Chat" -> ConversationScreen()
                "Setting" -> SettingScreen()
            }

        }
    }
}

/*@Composable
fun BottomNavigationBar(
    selectedScreen: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedScreen == "home",
            onClick = { onItemSelected("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Chat, contentDescription = "Settings") },
            label = { Text("Chat") },
            selected = selectedScreen == "Chat",
            onClick = { onItemSelected("Chat") }
        )
        // Add more items if needed
    }
}*/

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


@Composable
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
}




/*
package com.example.texttranslator.activities

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.texttranslator.screen.HomeScreen
import com.example.texttranslator.ui.theme.TextTranslatorTheme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.texttranslator.R
import com.example.texttranslator.viewmodels.HomeViewModel
import com.example.texttranslator.viewmodels.SpeechViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApp : Application()



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var speechViewModel: SpeechViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speechViewModel = ViewModelProvider(this)[SpeechViewModel::class.java]

        setContent {
            TextTranslatorTheme {
                MainScreen(
                    startSpeechToText = { selectedLang ->
                        startSpeechRecognition(selectedLang)
                    }
                )
            }
        }
    }

  private val speechLauncher =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
          if (result.resultCode == RESULT_OK) {
              val data = result.data
              val spokenText = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
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
fun MainScreen(
    startSpeechToText: (String) -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()  // <== Inject HomeViewModel via Hilt
    val speechViewModel: SpeechViewModel = hiltViewModel()
    val spokenText = speechViewModel.spokenText
    var selectedScreen by remember { mutableStateOf("Home") }

    // State for HomeScreen
    var isSwitchChecked by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }
    val context = LocalContext.current


    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedScreen = selectedScreen,
                onItemSelected = { selectedScreen = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                "Home" -> HomeScreen(
                    onSettingsClick = { */
/* Handle settings click *//*
 },
                    onSwitchToggle = { isSwitchChecked = it },
                    isSwitchChecked = isSwitchChecked,
                    onTransBtnClick = {
                        Log.d("sp_text",speechViewModel.spokenText )
                        viewModel.inputText = speechViewModel.spokenText // assign inputText before translate

                        viewModel.translateText()


                        // Handle Translate Button click
                    },
                    onVoiceInputClick = {
                        startSpeechToText(viewModel.firstLang)                                      },
                    onLangSwitch = {
                        // Handle Language Switch click
                    },
                    textInput = spokenText,
                    onTextChange = { speechViewModel.updateSpokenText(it)},
                    onLangSelected = { selectedLang ->

                        val intent = Intent(context, LanguageActivity::class.java)
                        intent.putExtra("selected_language", selectedLang)
                        context.startActivity(intent)
                    },
                    translatedText = viewModel.translatedText,
                    isTranslating = viewModel.isTranslating,
                    errorMessage = viewModel.errorMessage
                )

                "Chat" -> ChatScreen()
                "Camera" -> CameraScreen()
            }
        }
    }
}





data class NavItem(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

val navItems = listOf(
    NavItem("Home", R.drawable.home_icon, R.drawable.home_icon_outline),
    NavItem("Chat", R.drawable.chat_icon_filled, R.drawable.chat_icon_outline),
    NavItem("Camera", R.drawable.camera_icon_filled, R.drawable.camera_icon)
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


@Composable
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
}

*/
