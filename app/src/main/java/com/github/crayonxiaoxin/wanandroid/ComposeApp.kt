package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * 入口
 */
@Composable
fun ComposeApp(backDispatcher: OnBackPressedDispatcher) {
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