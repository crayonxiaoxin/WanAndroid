package com.github.crayonxiaoxin.wanandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.github.crayonxiaoxin.wanandroid.ui.theme.JetpackComposeTheme
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
            JetpackComposeTheme {
                ComposeApp(backDispatcher = onBackPressedDispatcher)
            }
        }
    }


}
