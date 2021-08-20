package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.github.crayonxiaoxin.wanandroid.ui.detail.DetailScreen
import com.github.crayonxiaoxin.wanandroid.ui.login.LoginScreen
import com.github.crayonxiaoxin.wanandroid.ui.main.MainScreen
import com.github.crayonxiaoxin.wanandroid.util.User
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import java.net.URLEncoder

/**
 * 返回栈调度器
 */
internal val LocalBackDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> {
    error("No back dispatcher provided")
}

/**
 * 导航图
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavMain(controller: NavHostController = rememberAnimatedNavController()) {
    val scope = rememberCoroutineScope()
    val startDestination = remember {
        mutableStateOf("main")
    }
    SideEffect {
        scope.launch {
            startDestination.value = if (User.isLogin()) "main" else "login"
        }
    }
    AnimatedNavHost(
        navController = controller,
        startDestination = startDestination.value,
        enterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(300)
            )
        },
        exitTransition = { _, _ ->
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(300)
            )
        },
        popEnterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(300)
            )
        },
        popExitTransition = { _, _ ->
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(300)
            )
        }
    ) {
        composable("main") { MainScreen(controller = controller) }
        composable("login") { LoginScreen(controller = controller) }
        composable(
            "detail/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
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