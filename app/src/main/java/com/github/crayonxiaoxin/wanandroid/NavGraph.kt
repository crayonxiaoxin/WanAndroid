package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.github.crayonxiaoxin.wanandroid.ui.detail.DetailScreen
import com.github.crayonxiaoxin.wanandroid.ui.login.LoginScreen
import com.github.crayonxiaoxin.wanandroid.ui.main.MainScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import java.net.URLEncoder

internal val LocalBackDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    error("No back dispatcher provided")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavMain(controller: NavHostController = rememberAnimatedNavController()) {
    AnimatedNavHost(navController = controller, startDestination = "main") {
        composable("main",
            enterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "login" ->
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            exitTransition = { _, target ->
                when (target.destination.route) {
                    "login" ->
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "login" ->
                        slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = { _, target ->
                when (target.destination.route) {
                    "login" ->
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            }
        ) { MainScreen(controller = controller) }
        composable("login") { LoginScreen(controller = controller) }
        composable(
            "detail/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType }),
            enterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "main" ->
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            exitTransition = { _, target ->
                when (target.destination.route) {
                    "main" ->
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popEnterTransition = { initial, _ ->
                when (initial.destination.route) {
                    "main" ->
                        slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700))
                    else -> null
                }
            },
            popExitTransition = { _, target ->
                when (target.destination.route) {
                    "main" ->
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700))
                    else -> null
                }
            }
        ) {
            DetailScreen(
                controller = controller,
                link = it.arguments?.getString("url").orEmpty()
            )
        }
    }
}

fun toDetail(controller: NavHostController, url: String?) {
    if (!url.isNullOrEmpty()) {
        controller.navigate("detail/${URLEncoder.encode(url, "UTF-8")}")
    }
}