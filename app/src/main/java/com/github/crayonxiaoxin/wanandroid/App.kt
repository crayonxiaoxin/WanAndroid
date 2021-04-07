package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun App(backDispatcher: OnBackPressedDispatcher) {
    CompositionLocalProvider(LocalBackDispatcher provides backDispatcher) {
        ProvideWindowInsets{
            NavMain()
        }
    }
}