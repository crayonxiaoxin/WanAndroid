package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoadStateLayout(
    state: LoadState = LoadState.Loading,
    loading: @Composable () -> Unit = { DefaultLoadingView() },
    retry: @Composable () -> Unit = { DefaultRetryView(onClick = retryOnClick) },
    retryOnClick: () -> Unit = {},
    empty: @Composable () -> Unit = { DefaultEmptyView() },
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        LoadStateFadeInOut(
            isShow = state == LoadState.Content,
            content = content,
            initiallyVisible = false // Note: 用于初始化（例如 webView loadUrl 需要在 content 中完成）
        )
        LoadStateFadeInOut(isShow = state == LoadState.Loading, content = loading)
        LoadStateFadeInOut(isShow = state == LoadState.Retry, content = retry)
        LoadStateFadeInOut(isShow = state == LoadState.Empty, content = empty)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LoadStateFadeInOut(
    isShow: Boolean,
    initiallyVisible: Boolean = false,
    content: @Composable() () -> Unit
) {
    AnimatedVisibility(
        visible = isShow,
        initiallyVisible = initiallyVisible,
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

enum class LoadState {
    Loading, Retry, Empty, Content
}