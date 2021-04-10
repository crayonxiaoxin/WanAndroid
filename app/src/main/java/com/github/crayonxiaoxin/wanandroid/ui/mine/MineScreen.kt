package com.github.crayonxiaoxin.wanandroid.ui.mine

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.ui.common.DefaultEmptyView
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadState
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadStateLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MineScreen(controller: NavHostController) {
    var state by remember {
        mutableStateOf(LoadState.Loading)
    }
    LaunchedEffect(key1 = 1) {
        delay(3000)
        state = LoadState.Empty
        delay(3000)
        state = LoadState.Retry
    }
    val scope = rememberCoroutineScope()
    LoadStateLayout(
        state = state,
        retryOnClick = {
            scope.launch {
                state = LoadState.Loading
                delay(3000)
                state = LoadState.Content
            }
        }
    ) {
        DefaultEmptyView("Content")
    }
}

