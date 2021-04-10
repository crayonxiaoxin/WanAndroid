package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadStateLayout(
    state: LoadState = LoadState.Loading,
    loading: @Composable () -> Unit = { DefaultLoadingView() },
    retry: @Composable () -> Unit = { DefaultRetryView(onClick = retryOnClick) },
    retryOnClick: () -> Unit = {},
    empty: @Composable () -> Unit = { DefaultEmptyView() },
    content: @Composable () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            LoadState.Loading -> loading()
            LoadState.Retry -> retry()
            LoadState.Empty -> empty()
            LoadState.Content -> content()
        }
    }
}

@Composable
fun DefaultLoadingView(label: String = "Loading") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Text(text = label, modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}

@Composable
fun DefaultRetryView(label: String = "Please retry", onClick: () -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onClick) {
            Text(text = label)
        }
    }
}

@Composable
fun DefaultEmptyView(label: String = "No Data") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = label)
    }
}

enum class LoadState {
    Loading, Retry, Empty, Content
}