package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun App(backDispatcher: OnBackPressedDispatcher) {
    CompositionLocalProvider(
        // 返回栈
        LocalBackDispatcher provides backDispatcher,
        // set fontScale = 1f, then 1.sp == 1.dp, 防止字体跟随系统变化
        LocalDensity provides Density(LocalDensity.current.density, fontScale = 1f)
    ) {
        ProvideWindowInsets {
            NavMain()
        }
    }
}