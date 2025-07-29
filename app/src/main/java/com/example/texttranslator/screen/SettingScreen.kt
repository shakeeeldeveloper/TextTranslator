package com.example.texttranslator.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.ui.text.font.FontWeight


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.texttranslator.R
import com.example.texttranslator.viewmodels.HomeViewModel
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.activity.compose.LocalActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onOpenHistory: () -> Unit
) {
    var showPrivacyPolicy by remember { mutableStateOf(false) }
    var showRateUs by remember { mutableStateOf(false) }
    var showShareApp by remember { mutableStateOf(false) }

    //val activity = LocalActivity.current
    val context = LocalContext.current

    val activity = context as? Activity
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(lifecycleOwner) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 👇 Move the app to background (home screen)
                activity?.moveTaskToBack(true)
            }
        }

        dispatcher?.addCallback(lifecycleOwner, callback)

        onDispose {
            callback.remove()
        }
    }
    if (showPrivacyPolicy){
        LaunchedEffect(Unit) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.freeprivacypolicy.com/live/841a0503-8798-444c-83da-4c50ecac913d"))
            context.startActivity(intent)
        }
    }
    // val viewModel: HomeViewModel = hiltViewModel()

/*    if (showHistory) {
        viewModel.loadHistory()
        HistoryScreen(
            historyItems = viewModel.historyList.value,
            onBack = { showHistory = false },
            onClear = { viewModel.clearHistory() },
            onDelete = { viewModel.deleteItem(it) }
        )
    }
    else {*/
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Setting",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )
                       /* Icon(
                            painter = painterResource(id = R.drawable.setting_icon_black),
                            contentDescription = "Settings",
                            modifier = Modifier.padding(end = 12.dp),
                            tint = Color.Unspecified
                        )*/
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                modifier = Modifier.shadow(4.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                SettingItem(
                    title = "History",
                    iconStart = R.drawable.history_icon,
                    onClick = { /*showHistory = true*/
                        onOpenHistory()
                    }
                )
                SettingItem(
                    title = "Rate Us",
                    iconStart = R.drawable.rate_us,
                    onClick = {

                    }
                )
                SettingItem(
                    title = "Privacy Policy",
                    iconStart = R.drawable.rate_us,
                    onClick = {
                        showPrivacyPolicy=true
                    }
                )
                SettingItem(
                    title = "Share App",
                    iconStart = R.drawable.share_icon,
                    onClick = {  }
                )
                SettingItem(
                    title = "About Us",
                    iconStart = R.drawable.about_us,
                    onClick = {  }
                )

            }
        }
    //}
}

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    title: String = "Change Language",
    subtitle: String = "Tap to change",
    iconStart: Int = R.drawable.back_icon,
    iconEnd: Int = R.drawable.next_icon,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconStart),
            contentDescription = "Start Icon",
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Image(
            painter = painterResource(id = iconEnd),
            contentDescription = "Next Icon",
            modifier = Modifier.size(18.dp)
        )
    }
}



/*
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import com.example.texttranslator.R // Replace with your actual package


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Setting",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.setting_icon),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            SettingItem(
                title = "History",
                iconStart = R.drawable.history_icon,
                onClick = {

                }

            )
            SettingItem()
            SettingItem()
        }
    }

}
@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    title: String = "Change Language",
    subtitle: String = "Tap to change",
    iconStart: Int = R.drawable.back_icon,
    iconEnd: Int = R.drawable.next_icon,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start Icon
        Image(
            painter = painterResource(id = iconStart),
            contentDescription = "Start Icon",
            modifier = Modifier
                .size(24.dp)
        )

        // Texts
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // End Icon
        Image(
            painter = painterResource(id = iconEnd),
            contentDescription = "Next Icon",
            modifier = Modifier
                .size(18.dp)
        )
    }
}*/


