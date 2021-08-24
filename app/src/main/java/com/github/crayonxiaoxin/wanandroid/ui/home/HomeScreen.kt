package com.github.crayonxiaoxin.wanandroid.ui.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.github.crayonxiaoxin.wanandroid.*
import com.github.crayonxiaoxin.wanandroid.data.LoadState
import com.github.crayonxiaoxin.wanandroid.data.Result
import com.github.crayonxiaoxin.wanandroid.data.isOK
import com.github.crayonxiaoxin.wanandroid.data.data
import com.github.crayonxiaoxin.wanandroid.ui.common.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@ExperimentalCoilApi
@OptIn(ExperimentalFoundationApi::class, InternalCoroutinesApi::class)
@ExperimentalPagerApi
@Composable
fun HomeScreen(controller: NavHostController, vm: HomeScreenVM = viewModel()) {
    val pageState by vm.pageState.observeAsState(PageState.Loading)
    val bannerState by vm.bannerState.observeAsState()
    val topArticleState by vm.topArticleState.observeAsState()
    val articleState by vm.articleState.observeAsState()
    val netState by vm.loadState.observeAsState()
    // Note: 状态改变是否会导致整个HomeScreen刷新？是的，那么需要确保不能重复获取数据
    if (bannerState == null) {
        vm.init()
    }

    PageStateLayout(
        state = pageState,
        retryOnClick = {
            vm.init(true)
        }
    ) {
        Scaffold {
            val isRefresh = netState == LoadState.Refresh
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefresh),
                onRefresh = { vm.init(true) }) {
                Column {
                    bannerState?.let { bs ->
                        if (bs.isOK) {
                            val banners = bs.data()
                            Banner(
                                bannerSize = banners.size,
                                bannerItem = { banners[it] },
                                onItemClick = { toDetail(controller, it.url) }
                            )
                        }
                    }
                    val homeListState = rememberLazyListState()
                    LazyColumn(state = homeListState) {
                        // Note: 这里 ?. 的操作是必需的，因为 State<T> 初始值为 null
                        topArticleState?.let { ats ->
                            if (ats.isOK) {
                                val articles = ats.data()
                                if (articles.isNotEmpty()) {
                                    stickyHeader(key = "h1") {
                                        HomeListHeader("置顶文章")
                                    }
                                    items(articles.size, key = { articles[it].id }) { item ->
                                        ArticleItem(
                                            data = articles[item],
                                            isTop = true,
                                            onItemClick = {
                                                toDetail(controller = controller, url = it.link)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        articleState?.let { ats ->
                            if (ats.isOK) {
                                val articles = ats.data()
                                if (articles.isNotEmpty()) {
                                    stickyHeader(key = "h2") {
                                        HomeListHeader("最新文章")
                                    }
                                    items(
                                        count = articles.size,
                                        key = { articles[it].id }) { item ->
                                        ArticleItem(
                                            data = articles[item],
                                            onItemClick = {
                                                toDetail(controller = controller, url = it.link)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        item(key = "footer") {
                            // Note: 根据 articleNetState 展示不同的 footer
                            LazyListFooter(
                                loadState = vm.articleLoadState,
                                isTheEnd = vm.articleCurrentPage == vm.articleTotalPage,
                                retry = { vm.getArticles() }
                            )
                        }
                    }

                    LaunchedEffect(key1 = "lazyBottom") {
                        // Note: snapshotFlow 防止不必要的重组
                        snapshotFlow {
                            homeListState.firstVisibleItemIndex
                        }.distinctUntilChanged().collect { firstVisibleItemIndex ->
                            vm.articleState.value?.let {
                                // Note: 上拉加载，到达底部，获取下一页数据
                                if (it.isOK) {
                                    if (firstVisibleItemIndex == vm.articleLastSize) {
                                        vm.getArticles()
                                        Log.e("TAG", "HomeScreen: 1")
                                    }
                                    Log.e(
                                        "TAG",
                                        "HomeScreen: 2  firstVisibleItemIndex = $firstVisibleItemIndex  , size = ${(it as Result.Success).data.size} , lastSize = ${vm.articleLastSize}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeListHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFF2F2F2))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
