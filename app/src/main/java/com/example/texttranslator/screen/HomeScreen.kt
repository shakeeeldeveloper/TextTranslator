package com.example.texttranslator.screen

import android.R.attr.text
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import androidx.compose.runtime.saveable.rememberSaveable

import com.example.texttranslator.R
import com.example.texttranslator.activities.LanguageActivity
import com.example.texttranslator.activities.TranslationActivity


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit = {},
    onSwitchToggle: (Boolean) -> Unit = {},
    isSwitchChecked: Boolean = false,
    onTransBtnClick: () -> Unit = {},
    onVoiceInputClick: () -> Unit = {},
    onLangSwitch: () -> Unit = {},
    textInput: String = "",
    onTextChange: (String) -> Unit = {},
    onLangSelected:  (String) -> Unit = {},
    translatedText: String,
    isTranslating: Boolean,
    errorMessage: String

) {

    when {
        isTranslating -> Text("Translating...")
        translatedText.isNotEmpty() -> Text("Translated: $translatedText")
        errorMessage.isNotEmpty() -> Text("Error: $errorMessage")
    }
    var selectedLanguage by remember { mutableStateOf("") }

    val context = LocalContext.current

    val originalText = viewModel.inputText
    val translatedText = viewModel.translatedText
    val sourceLang = viewModel.firstLang
    val targetLang = viewModel.secondLang
    LaunchedEffect(translatedText) {
        if (translatedText.isNotEmpty()) {

            Log.d("lang",sourceLang+"   in home $targetLang    $originalText     $translatedText")

            val intent = Intent(context, TranslationActivity::class.java).apply {
                putExtra("original_text", originalText)
                putExtra("translated_text", translatedText)
                putExtra("source_lang", viewModel.firstLang)
                putExtra("target_lang", viewModel.secondLang)
            }
            context.startActivity(intent)

            viewModel.clearTranslation()
        }
    }
    val languageActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val language = data?.getStringExtra("selected_language") ?: ""
            viewModel.updateSelectedLanguage(language)
        }
    }
    val firstLang = viewModel.firstLang
    val secondLang = viewModel.secondLang

    Scaffold { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // <-- apply innerPadding
                .padding(16.dp)
        ) {
            // Header with Title and Settings Icon
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Text Translator",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                /*Icon(
                    painter = painterResource(id = R.drawable.setting_icon),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(27.dp)
                        .clickable {
                            showSettings = true
                            onSettingsClick()
                        }
                )*/
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Translator Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF80BDFF)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Quick Translator",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Translate your text into countless languages from across the globe.",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    /*CustomSwitch(
                        isChecked = isSwitchChecked,
                        onCheckedChange = onSwitchToggle
                    )*/
                }


            }

            Spacer(modifier = Modifier.height(15.dp))

            // Main Card for Translator Functionality
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(329.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    // Language Selection Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LanguageButton(firstLang) {
                            viewModel.currentLangType="first"
                            val intent = Intent(context, LanguageActivity::class.java)
                            languageActivityLauncher.launch(intent)
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.rotate_icon),
                            contentDescription = "Switch Language",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    // Swap logic
                                    viewModel.swapLanguages() // optional: if you need external handling
                                }
                        )

                        LanguageButton(secondLang) {
                            viewModel.currentLangType="second"
                            val intent = Intent(context, LanguageActivity::class.java)
                            languageActivityLauncher.launch(intent)
                        }
                    }


                    TextField(
                        value = textInput,
                        onValueChange = onTextChange,
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



                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(
                            id = if (textInput.isNotEmpty()) R.drawable.trans_svg else R.drawable.voice_icon
                        ),
                        contentDescription = if (textInput.isNotEmpty()) "Translate" else "Voice Input",
                        modifier = Modifier

                            .align(Alignment.End)
                            .clickable {
                                if (textInput.isNotEmpty()) {
                                    onTransBtnClick()
                                } else {
                                    onVoiceInputClick()
                                }
                            }
                    )

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {
                isTranslating -> Text(
                    text = "Translating...",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                translatedText.isNotEmpty() -> Text(



                    text = "Translated: $translatedText",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                )
                errorMessage.isNotEmpty() -> Text(
                    text = "Error: $errorMessage",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}



/*@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit = {},
    onSwitchToggle: (Boolean) -> Unit = {},
    isSwitchChecked: Boolean = false,
    onTransBtnClick: () -> Unit = {},
    onLangSwitch: () -> Unit = {},
    textInput: String = "",
    onTextChange: (String) -> Unit = {},
    onLangSelected:  (String) -> Unit = {}

) {



    var selectedLanguage by remember { mutableStateOf("") }
    var firstLang by remember { mutableStateOf("English") }
    var secondLang by remember { mutableStateOf("Urdu") }
    var currentLangType by remember { mutableStateOf("") }
    val context = LocalContext.current



    val languageActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val language = data?.getStringExtra("selected_language") ?: ""

            if (currentLangType == "first") {
                firstLang = language
            } else if (currentLangType == "second") {
                secondLang = language
            }
        }
    }

    Scaffold { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // <-- apply innerPadding
                .padding(16.dp)
        ) {
            // Header with Title and Settings Icon
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Translator",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    painter = painterResource(id = R.drawable.setting_icon),
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(27.dp)
                        .clickable { onSettingsClick() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Translator Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF80BDFF)
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(1f)
                    ) {
                        Text(
                            text = "Quick Translator",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Using Floating icon Translate chat, Text, picture, full screen text, video/game sub-titles and more.",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    CustomSwitch(
                        isChecked = isSwitchChecked,
                        onCheckedChange = onSwitchToggle
                    )
                }

                */
/* Row (
                     modifier= Modifier
                     .fillMaxWidth()
                 ) {


                     Column(
                         modifier = Modifier
                             .wrapContentWidth()
                             .padding(16.dp),
                         verticalArrangement = Arrangement.SpaceBetween
                     ) {
                         Text(
                             text = "Quick Translator",
                             fontSize = 16.sp,
                             fontWeight = FontWeight.Bold,
                             color = Color.White
                         )

                         Text(
                             text = "Using Floating icon Translate chat, Text, picture, full screen text, video/game sub-titles and more.",
                             fontSize = 13.sp,
                             color = Color.White
                         )


                     }
                     Column (
                         modifier = Modifier
                             .padding(end = 16.dp)
                     ){
                         CustomSwitch(
                             isChecked = isSwitchChecked,
                             onCheckedChange = onSwitchToggle
                         )
                     }

                 }*/
/*
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Main Card for Translator Functionality
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(329.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    // Language Selection Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LanguageButton(firstLang) {
                            currentLangType = "first"
                            val intent = Intent(context, LanguageActivity::class.java)
                            languageActivityLauncher.launch(intent)
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.rotate_icon),
                            contentDescription = "Switch Language",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    // Swap logic
                                    val temp = firstLang
                                    firstLang = secondLang
                                    secondLang = temp

                                    onLangSwitch() // optional: if you need external handling
                                }
                        )

                        LanguageButton(secondLang) {
                            currentLangType = "second"
                            val intent = Intent(context, LanguageActivity::class.java)
                            languageActivityLauncher.launch(intent)
                        }
                    }


                    TextField(
                        value = textInput,
                        onValueChange = onTextChange,
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



                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(
                            id = if (textInput.isNotEmpty()) R.drawable.trans_svg else R.drawable.voice_icon
                        ),
                        contentDescription = if (textInput.isNotEmpty()) "Translate" else "Voice Input",
                        modifier = Modifier

                            .align(Alignment.End)
                            .clickable { onTransBtnClick() }
                    )

                }
            }
        }
    }
}*/

@Composable
fun CustomSwitch(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Switch(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier
            .width(97.dp)
            .height(35.dp)
            .padding(horizontal = 12.dp),
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.Gray, // Placeholder color, replace with your drawable if needed
            checkedTrackColor = Color.LightGray
        )
    )
}

@Composable
fun LanguageButton(language: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7F3FF)),
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier
            .width(112.dp)
            .height(48.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE7F3FF)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = language,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

