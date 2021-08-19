package com.github.crayonxiaoxin.wanandroid.ui.mine

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.ui.common.DefaultEmptyView
import com.github.crayonxiaoxin.wanandroid.ui.common.PageStateLayout
import com.github.crayonxiaoxin.wanandroid.ui.common.PageState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MineScreen(controller: NavHostController) {
    var state by remember {
        mutableStateOf<PageState>(PageState.Loading)
    }
    LaunchedEffect(key1 = 1) {
        delay(3000)
        state = PageState.Empty
        delay(3000)
        state = PageState.Retry
    }
    val scope = rememberCoroutineScope()
    PageStateLayout(
        state = state,
        retryOnClick = {
            scope.launch {
                state = PageState.Loading
                delay(3000)
                state = PageState.Content
            }
        }
    ) {
        DefaultEmptyView("Content")
    }
}

