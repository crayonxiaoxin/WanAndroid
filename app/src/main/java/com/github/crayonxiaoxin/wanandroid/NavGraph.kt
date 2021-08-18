package com.github.crayonxiaoxin.wanandroid

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.*
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument
import com.github.crayonxiaoxin.wanandroid.ui.detail.DetailScreen
import com.github.crayonxiaoxin.wanandroid.ui.login.LoginScreen
import com.github.crayonxiaoxin.wanandroid.ui.main.MainScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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
    AnimatedNavHost(navController = controller, startDestination = "main") {
        animatedComposable("main") { MainScreen(controller = controller) }
        animatedComposable("login") { LoginScreen(controller = controller) }
        animatedComposable(
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

/**
 * 导航 - 默认动画
 */
@ExperimentalAnimationApi
fun NavGraphBuilder.animatedComposable(
    route: String,
    transitionDuration: Int = 300,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (
        (initial: NavBackStackEntry, target: NavBackStackEntry) -> EnterTransition?
    )? = { _, _ ->
        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(transitionDuration))
    },
    exitTransition: (
        (initial: NavBackStackEntry, target: NavBackStackEntry) -> ExitTransition?
    )? = { _, _ ->
        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(transitionDuration))
    },
    popEnterTransition: (
        (initial: NavBackStackEntry, target: NavBackStackEntry) -> EnterTransition?
    )? = { _, _ ->
        slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(transitionDuration))
    },
    popExitTransition: (
        (initial: NavBackStackEntry, target: NavBackStackEntry) -> ExitTransition?
    )? = { _, _ ->
        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(transitionDuration))
    },
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route,
        arguments,
        deepLinks,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
        content
    )
}

fun toDetail(controller: NavHostController, url: String?) {
    if (!url.isNullOrEmpty()) {
        controller.navigate("detail/${URLEncoder.encode(url, "UTF-8")}")
    }
}