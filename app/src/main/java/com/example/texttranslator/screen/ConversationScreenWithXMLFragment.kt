package com.example.texttranslator.screen

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.texttranslator.ChatFragment

@Composable
fun ConversationScreenWithXMLFragment() {
    val context = LocalContext.current
    val activity = context as AppCompatActivity

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            // Create a container to host the fragment
            val container = FrameLayout(ctx).apply {
                id = View.generateViewId()
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }

            // Add your XML Fragment to this container
            val fragmentManager = activity.supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(container.id, ChatFragment())
            fragmentTransaction.commitNowAllowingStateLoss()

            container
        }
    )
}
