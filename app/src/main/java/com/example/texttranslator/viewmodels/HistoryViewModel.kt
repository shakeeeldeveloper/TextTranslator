package com.example.texttranslator.viewmodels


import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.example.texttranslator.model.HistoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel


@HiltViewModel
class HistoryViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val historyPrefs = HistoryPreferences(application)

    private val _historyList = mutableStateListOf<HistoryEntity>()
    val historyList: List<HistoryEntity> = _historyList

    init {
        loadHistory()
    }

    fun loadHistory() {
        _historyList.clear()
        _historyList.addAll(historyPrefs.getHistory())
    }

    fun addHistory(item: HistoryEntity) {
        historyPrefs.saveHistory(item)
        loadHistory()
    }

    fun clearHistory() {
        historyPrefs.clearHistory()
        _historyList.clear()
    }
}



/*@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    val historyList: LiveData<List<HistoryEntity>> = repository.allHistory.asLiveData()

    fun insertHistory(
        sourceText: String,
        translatedText: String,
        sourceLangCode: String,
        targetLangCode: String
    ) {
        viewModelScope.launch {
            val history = HistoryEntity(
                sourceText = sourceText,
                translatedText = translatedText,
                sourceLangCode = sourceLangCode,
                targetLangCode = targetLangCode
            )
            repository.insertHistory(history)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}*/
