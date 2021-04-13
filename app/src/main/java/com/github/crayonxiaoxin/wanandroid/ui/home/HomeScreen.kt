package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.github.crayonxiaoxin.wanandroid.*
import com.github.crayonxiaoxin.wanandroid.data.NetState
import com.github.crayonxiaoxin.wanandroid.ui.common.Banner
import com.github.crayonxiaoxin.wanandroid.ui.common.LazyListFooter
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadState
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadStateLayout
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalFoundationApi::class, InternalCoroutinesApi::class)
@ExperimentalPagerApi
@Composable
fun HomeScreen(controller: NavHostController, vm: HomeScreenVM = viewModel()) {
    var state by remember {
        mutableStateOf(LoadState.Content)
    }
    // Note: banner加载完成就可以 show content
    state = when (vm.bannerNetState) {
        NetState.Loading -> LoadState.Loading
        NetState.Success -> LoadState.Content
        is NetState.Error -> LoadState.Retry
        else -> LoadState.Loading
    }
    val bannerState by vm.bannerList.observeAsState()
    val topArticleState by vm.topArticleList.observeAsState()
    val articleState by vm.articleList.observeAsState()
    // Note: 状态改变是否会导致整个HomeScreen刷新？是的，那么需要确保不能重复获取数据
    if (bannerState == null) {
        vm.init()
    }

    LoadStateLayout(
        state = state,
        retryOnClick = {
            vm.init(true)
        }
    ) {
        Scaffold {
            Column {
                bannerState?.let { banners ->
                    Banner(
                        bannerSize = banners.size,
                        bannerItem = { banners.get(it) },
                        onItemClick = {}
                    )
                }
                val homeListState = rememberLazyListState()
                LazyColumn(state = homeListState) {
                    // Note: 这里 ?. 的操作是必需的，因为 State<T> 初始值为 null
                    topArticleState?.let { articles ->
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

                    articleState?.let { articles ->
                        if (articles.isNotEmpty()) {
                            stickyHeader(key = "h2") {
                                HomeListHeader("最新文章")
                            }
                            items(count = articles.size, key = { articles[it].id }) { item ->
                                ArticleItem(
                                    data = articles[item],
                                    onItemClick = {
                                        toDetail(controller = controller, url = it.link)
                                    }
                                )
                            }
                        }
                    }

                    item(key = "footer") {
                        // Note: 根据 articleNetState 展示不同的 footer
                        LazyListFooter(
                            netState = vm.articleNetState,
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
                        vm.articleList.value?.let {
                            // Note: 上拉加载，到达底部，获取下一页数据
                            if (firstVisibleItemIndex == it.size) {
                                vm.getArticles()
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

@Composable
private fun HomeTopBar() {
    Box(Modifier.background(color = MaterialTheme.colors.primary)) {
        TopAppBar(
            modifier = Modifier
                .statusBarsPadding()
                .background(color = Color.Transparent)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "首頁")
            }
        }
    }
}