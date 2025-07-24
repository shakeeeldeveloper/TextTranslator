package com.example.texttranslator.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.texttranslator.repositories.TranslationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val translationRepository: TranslationRepository

) : ViewModel(

) {

    var firstLang by mutableStateOf("English")
        private set

    var secondLang by mutableStateOf("Urdu")
        private set

     var currentLangType by mutableStateOf("")
    private val _translated = MutableStateFlow("")
    val translated: StateFlow<String> = _translated

    var inputText by mutableStateOf("")
    var translatedText by mutableStateOf("")
    var isTranslating by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun swapLanguages() {

        Log.d("cur", "swipe in viewmodel")

        val temp = firstLang
        firstLang = secondLang
        secondLang = temp
    }

    fun updateSelectedLanguage(language: String) {
        if (currentLangType == "first") {
            firstLang = language
        } else if (currentLangType == "second") {
            Log.d("cur",language+" true "+currentLangType)

            secondLang = language
        }
    }
    fun translateText() {
        Log.d("transll", inputText+" "+ firstLang +" " +secondLang)

        if (inputText.isEmpty()) return

        isTranslating = true
        errorMessage = ""

        viewModelScope.launch {
            try {
                val result = translationRepository.translateText(inputText, firstLang, secondLang)
                isTranslating = false

                Log.d("transll", inputText + " " + firstLang + " " + secondLang)

                result.onSuccess {
                    translatedText = it
                    _translated.value = it
                }.onFailure {
                    errorMessage = it.message ?: "Translation failed"
                }
            } catch (e: Exception) {
                isTranslating = false
                errorMessage = e.message ?: "Unexpected error"
                Log.e("transll", "Exception during translation: ${e.message}", e)
            }
        }
    }
    fun clearTranslation() {
        translatedText = ""
        _translated.value=""
    }
    fun setLanguages(first: String, second: String) {
        firstLang = first
        secondLang = second
    }

    /*fun setCurrentLangType(type: String) {
        currentLangType = type
    }*/
}