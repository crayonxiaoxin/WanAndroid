package com.github.crayonxiaoxin.wanandroid.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Subject
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.R
import com.github.crayonxiaoxin.wanandroid.ui.daohang.DaoHangScreen
import com.github.crayonxiaoxin.wanandroid.ui.home.HomeScreen
import com.github.crayonxiaoxin.wanandroid.ui.home.HomeScreenVM
import com.github.crayonxiaoxin.wanandroid.ui.mine.MineScreen
import com.github.crayonxiaoxin.wanandroid.ui.tixi.TixiScreen
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(controller: NavHostController) {
    val pagerState =
        rememberPagerState(pageCount = HomeTabs.values().size, initialOffscreenLimit = 1)
    val scope = rememberCoroutineScope()
    val homeVm: HomeScreenVM = viewModel()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Scaffold(
            bottomBar = { BottomBar(pagerState, scope) },
            modifier = Modifier.navigationBarsPadding() // 防止被虚拟导航覆盖
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                dragEnabled = true,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) { page ->
                val tab = HomeTabs.values()[page]
                Box(
                    Modifier.fillMaxSize()
                ) {
                    when (tab) {
                        HomeTabs.HOME -> HomeScreen(controller, homeVm)
                        HomeTabs.DAOHANG -> {
//                            Text(
//                                tab.name, modifier = Modifier
//                                    .padding(32.dp)
//                                    .fillMaxWidth()
//                            )
                            DaoHangScreen(controller)
                        }
                        HomeTabs.MINE -> MineScreen(controller)
                        HomeTabs.TIXI -> TixiScreen(controller)
                        else -> Text(
                            tab.name, modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth()
                        )
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
//                        pagerState.animateScrollToPage(HomeTabs.values().indexOf(tab))
                        pagerState.scrollToPage(HomeTabs.values().indexOf(tab))
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