package com.github.crayonxiaoxin.wanandroid.ui.home

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.R
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(controller: NavHostController) {
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(HomeTabs.HOME) }
    val pagerState = rememberPagerState(pageCount = HomeTabs.values().size)
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primarySurface)
    ) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                TopAppBar(title = { Text("HomeScreen") })
            },
            bottomBar = {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primarySurface
                ) {
                    HomeTabs.values().forEach { tab ->
                        BottomNavigationItem(
                            selected = (tab == selectedTab || tab == HomeTabs.values()[pagerState.currentPage]),
                            onClick = {
                                setSelectedTab(tab)
                                scope.launch {
                                    Log.e("TAG", "HomeScreen: " + HomeTabs.values().indexOf(tab))
                                    pagerState.scrollToPage(HomeTabs.values().indexOf(tab))
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = stringResource(id = tab.title)
                                )
                            },
                            label = {
                                Text(text = stringResource(tab.title))
                            }
                        )
                    }
                }
            }
        ) {

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                val tab = HomeTabs.values()[page]
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.secondary)
                ) {
                    Text(tab.name)
                }
            }
        }
    }


}

private enum class HomeTabs(@StringRes val title: Int, val icon: ImageVector) {
    HOME(R.string.tab_home, Icons.Filled.Home),
    TIXI(R.string.tab_tixi, Icons.Filled.Subject),
    DAOHANG(R.string.tab_daohang, Icons.Filled.LocationOn),
    MINE(R.string.tab_mine, Icons.Filled.Face)
}