package com.github.crayonxiaoxin.wanandroid

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.github.crayonxiaoxin.wanandroid.ui.theme.WanAndroidTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkIcons = true
            SideEffect {
                // >= api 29 時，darkIcons 不生效, color 可以生效
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = darkIcons)
                setSystemBarsDarkIcons(darkIcons)
            }
            WanAndroidTheme {
                App(backDispatcher = onBackPressedDispatcher)
            }
        }
    }


}
