package com.example.texttranslator.repositories


import android.R.id.input
import android.content.Context
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import android.util.Log
/*import com.example.texttranslator.dao.TranslationDao
import com.example.texttranslator.model.HistoryEntity*/
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.Flow


suspend fun <T> com.google.android.gms.tasks.Task<T>.await(): T =
    suspendCancellableCoroutine { cont ->
        addOnSuccessListener { result -> cont.resume(result) }
        addOnFailureListener { e -> cont.resumeWithException(e) }
    }

class TranslationRepository @Inject constructor(
    @ApplicationContext private val context: Context

) {

    suspend fun translateText(text: String, sourceLang: String, targetLang: String): Result<String> {
        return try {
            Log.d("transll", "Translating: $text from $sourceLang to $targetLang")

            val sourceLangCode = TranslateLanguage.fromLanguageTag(getLanguageCode(sourceLang)) ?: TranslateLanguage.ENGLISH
            val targetLangCode = TranslateLanguage.fromLanguageTag(getLanguageCode(targetLang)) ?: TranslateLanguage.URDU

            val translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode)
                .setTargetLanguage(targetLangCode)
                .build()

            val translator = Translation.getClient(translatorOptions)

            Log.d("transll", "Checking model download...")

            val downloadTask = translator.downloadModelIfNeeded()

            downloadTask
                .addOnSuccessListener {
                    Log.d("transll", "Model download success ✅")
                }
                .addOnFailureListener { e ->
                    Log.e("transll", "Model download failed ❌: ${e.message}")
                }

            // Await ensures coroutine suspends until download finishes
            downloadTask.await()

            Log.d("transll", "Model download completed, translating text...")

            val translatedText = translator.translate(text).await()

            Log.d("transll", "Translation completed: $translatedText")

            translator.close()

           // Result.success(translatedText)

            /*dao.insertHistory(
                HistoryEntity(
                    sourceText = text,
                    translatedText = translatedText,
                    sourceLangCode = sourceLang,
                    targetLangCode = targetLang
                )
            )*/

            Result.success(translatedText)
        } catch (e: Exception) {
            Result.failure(e)
        }



        }
        /*catch (e: Exception) {
            Log.e("transll", "Translation failed with exception: ${e.message}")
            Result.failure(e)
        }*/

   /* fun getTranslationHistory(): Flow<List<HistoryEntity>> = dao.getHistory()
    suspend fun clearHistory() = dao.clearHistory()*/


    private fun getLanguageCode(language: String): String {
        return when (language) {
            "Afrikaans" -> "af"
            "Arabic" -> "ar"
            "Belarusian" -> "be"
            "Bengali" -> "bn"
            "Bulgarian" -> "bg"
            "Catalan" -> "ca"
            "Chinese" -> "zh"
            "Croatian" -> "hr"
            "Czech" -> "cs"
            "Danish" -> "da"
            "Dutch" -> "nl"
            "English" -> "en"
            "Esperanto" -> "eo"
            "Estonian" -> "et"
            "Finnish" -> "fi"
            "French" -> "fr"
            "Galician" -> "gl"
            "German" -> "de"
            "Greek" -> "el"
            "Gujarati" -> "gu"
            "Hebrew" -> "he"
            "Hindi" -> "hi"
            "Hungarian" -> "hu"
            "Icelandic" -> "is"
            "Indonesian" -> "id"
            "Irish" -> "ga"
            "Italian" -> "it"
            "Japanese" -> "ja"
            "Kannada" -> "kn"
            "Korean" -> "ko"
            "Latvian" -> "lv"
            "Lithuanian" -> "lt"
            "Macedonian" -> "mk"
            "Malay" -> "ms"
            "Marathi" -> "mr"
            "Norwegian" -> "no"
            "Persian" -> "fa"
            "Polish" -> "pl"
            "Portuguese" -> "pt"
            "Romanian" -> "ro"
            "Russian" -> "ru"
            "Slovak" -> "sk"
            "Slovenian" -> "sl"
            "Spanish" -> "es"
            "Swahili" -> "sw"
            "Swedish" -> "sv"
            "Tamil" -> "ta"
            "Telugu" -> "te"
            "Thai" -> "th"
            "Turkish" -> "tr"
            "Ukrainian" -> "uk"
            "Urdu" -> "ur"
            "Vietnamese" -> "vi"
            "Welsh" -> "cy"
            else -> "en" // default to English
        }
    }
}
