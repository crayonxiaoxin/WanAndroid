package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.crayonxiaoxin.wanandroid.ui.home.HomeScreen
import com.github.crayonxiaoxin.wanandroid.ui.login.LoginScreen

internal val LocalBackDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    error("No back dispatcher provided")
}

@Composable
fun NavMain(controller: NavHostController = rememberNavController()) {
    NavHost(navController = controller, startDestination = "login") {
        composable("home") { HomeScreen(controller = controller) }
        composable("login") { LoginScreen(controller = controller) }
    }
}