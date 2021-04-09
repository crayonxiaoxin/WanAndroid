package com.github.crayonxiaoxin.wanandroid.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Subject
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.R
import com.github.crayonxiaoxin.wanandroid.ui.home.HomeScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@ExperimentalPagerApi
@Composable
fun MainScreen(controller: NavHostController) {
    val pagerState = rememberPagerState(pageCount = HomeTabs.values().size)
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primarySurface)
    ) {
        Scaffold(
            bottomBar = { BottomBar(pagerState, scope) }
        ) {
            HorizontalPager(
                state = pagerState,
                dragEnabled = false,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val tab = HomeTabs.values()[page]
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.secondary)
                ) {
                    if (tab == HomeTabs.HOME) {
                        HomeScreen(controller)
                    } else {
                        Text(tab.name)
                    }
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun BottomBar(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primarySurface
    ) {
        HomeTabs.values().forEach { tab ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.LightGray,
                selected = (HomeTabs.values().indexOf(tab) == pagerState.currentPage),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(HomeTabs.values().indexOf(tab))
                    }
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = stringResource(id = tab.title)
                    )
                },
                label = { Text(text = stringResource(tab.title)) }
            )
        }
    }
}

private enum class HomeTabs(@StringRes val title: Int, val icon: ImageVector) {
    HOME(R.string.tab_home, Icons.Filled.Home),
    TIXI(R.string.tab_tixi, Icons.Filled.Subject),
    DAOHANG(R.string.tab_daohang, Icons.Filled.LocationOn),
    MINE(R.string.tab_mine, Icons.Filled.Face)
}