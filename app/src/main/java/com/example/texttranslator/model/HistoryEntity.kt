package com.example.texttranslator.model

data class HistoryEntity(
    val sourceText: String,
    val translatedText: String,
    val sourceLangCode: String,
    val targetLangCode: String,
    val timestamp: Long = System.currentTimeMillis()
)
