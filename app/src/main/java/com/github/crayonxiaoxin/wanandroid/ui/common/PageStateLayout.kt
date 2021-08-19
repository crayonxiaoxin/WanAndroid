package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 页面状态切换
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PageStateLayout(
        state: PageState = PageState.Loading,
        loading: @Composable () -> Unit = { DefaultLoadingView() },
        retry: @Composable () -> Unit = { DefaultRetryView(onClick = retryOnClick) },
        retryOnClick: () -> Unit = {},
        empty: @Composable () -> Unit = { DefaultEmptyView() },
        alwaysShowContent: Boolean = false, // 用在状态在content中更改的情况，例如 WebView
        content: @Composable () -> Unit = {}
) {
    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background)
    ) {
        if (alwaysShowContent) {
            content()
        } else {
            PageStateFadeInOut(isShow = state == PageState.Content, content = content)
        }
        PageStateFadeInOut(isShow = state == PageState.Loading, content = loading)
        PageStateFadeInOut(isShow = state == PageState.Retry, content = retry)
        PageStateFadeInOut(isShow = state == PageState.Empty, content = empty)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PageStateFadeInOut(
        isShow: Boolean = true,
        initiallyVisible: Boolean = false,
        state: MutableTransitionState<Boolean> = MutableTransitionState(initiallyVisible).apply {
            targetState = isShow
        },
        content: @Composable () -> Unit
) {
    AnimatedVisibility(
            visibleState = state,
            enter = fadeIn(animationSpec = tween(easing = FastOutLinearInEasing)),
            exit = fadeOut(animationSpec = tween(easing = LinearOutSlowInEasing))
    ) {
        content()
    }
}

@Composable
fun DefaultLoadingView(label: String = "Loading") {
    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Text(text = label, modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}

@Composable
fun DefaultRetryView(label: String = "Please retry", onClick: () -> Unit = {}) {
    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClick) {
            Text(text = label)
        }
    }
}

@Composable
fun DefaultEmptyView(label: String = "No Data") {
    Box(
            modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
    ) {
        Text(text = label)
    }
}

sealed class PageState {
    object Loading : PageState()
    object Retry : PageState()
    object Empty : PageState()
    object Content : PageState()
}