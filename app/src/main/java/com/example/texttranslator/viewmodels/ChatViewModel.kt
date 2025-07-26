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
class ChatViewModel @Inject constructor(
    private val translationRepository: TranslationRepository

) : ViewModel(

) {



    var firsttext = mutableStateOf("Speak Text")
    var secondtext = mutableStateOf("Speak Text")

     var currentSpeaker by mutableStateOf("second")


    var inputText by mutableStateOf("")
    var translatedText by mutableStateOf("")



 /*   fun updateSpeaker(speaker: String) {
        if (currentSpeaker == "first") {
            currentSpeaker = "first"
        } else if (currentSpeaker == "second") {

            currentSpeaker = speaker
        }
    }*/

    fun setTexts(first: String, second: String) {
        firsttext.value = first
        secondtext.value = second
    }


}