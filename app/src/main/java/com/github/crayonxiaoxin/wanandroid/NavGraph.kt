package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.github.crayonxiaoxin.wanandroid.ui.detail.DetailScreen
import com.github.crayonxiaoxin.wanandroid.ui.login.LoginScreen
import com.github.crayonxiaoxin.wanandroid.ui.main.MainScreen

internal val LocalBackDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    error("No back dispatcher provided")
}

@Composable
fun NavMain(controller: NavHostController = rememberNavController()) {
    NavHost(navController = controller, startDestination = "main") {
        composable("main") { MainScreen(controller = controller) }
        composable("login") { LoginScreen(controller = controller) }
        composable("detail/{url}") {
            DetailScreen(controller = controller, link = it.arguments?.getString("url").orEmpty())
        }
    }
}

fun toDetail(controller: NavHostController, url: String?) {
    if (!url.isNullOrEmpty()) {
        controller.navigate("detail/$url")
    }
}