package com.github.crayonxiaoxin.wanandroid.ui.detail

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailScreen(controller: NavHostController, link: String) {
    Scaffold(
        topBar = {
            DetailTopBar(
                modifier = Modifier.background(color = MaterialTheme.colors.primary),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "详情")
                    }
                }
            )
        }
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.run {
                        javaScriptEnabled = true
                        setSupportZoom(false)
                        javaScriptCanOpenWindowsAutomatically = false
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            it.loadUrl(link)
        }
    }
}

@Composable
private fun DetailTopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "详情")
        }
    },
    navigationIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "",
            modifier = Modifier.clickable { }
        )
    },
    actions: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        TopAppBar(
            modifier = Modifier
                .statusBarsPadding()
                .background(color = Color.Transparent),
        ) {
            Box {
                title()
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navigationIcon()
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        }
    }
}