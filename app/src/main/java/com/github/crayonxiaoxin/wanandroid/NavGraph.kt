package com.github.crayonxiaoxin.wanandroid

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.crayonxiaoxin.wanandroid.ui.home.HomeScreen
import com.github.crayonxiaoxin.wanandroid.ui.login.LoginScreen

@Composable
fun NavMain(controller: NavHostController = rememberNavController()) {
    NavHost(navController = controller, startDestination = "home") {
        composable("home") { HomeScreen(controller = controller) }
        composable("login") { LoginScreen(controller = controller) }
    }
}