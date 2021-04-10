package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.crayonxiaoxin.wanandroid.ui.login.LoginScreen
import com.github.crayonxiaoxin.wanandroid.ui.main.MainScreen

internal val LocalBackDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    error("No back dispatcher provided")
}

internal const val HomeBannerInterval: Long = 3000L
internal const val HomeBannerMaxCount = 10000
internal const val HomeBannerAspectRatio = 0.55f

@Composable
fun NavMain(controller: NavHostController = rememberNavController()) {
    NavHost(navController = controller, startDestination = "main") {
        composable("main") { MainScreen(controller = controller) }
        composable("login") { LoginScreen(controller = controller) }
    }
}