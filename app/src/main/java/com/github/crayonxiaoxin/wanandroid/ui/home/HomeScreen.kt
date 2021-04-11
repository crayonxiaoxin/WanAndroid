package com.github.crayonxiaoxin.wanandroid.ui.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.github.crayonxiaoxin.wanandroid.HomeBannerAspectRatio
import com.github.crayonxiaoxin.wanandroid.HomeBannerInterval
import com.github.crayonxiaoxin.wanandroid.HomeBannerMaxCount
import com.github.crayonxiaoxin.wanandroid.data.NetState
import com.github.crayonxiaoxin.wanandroid.model.ArticleData
import com.github.crayonxiaoxin.wanandroid.model.HomeBannerData
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadState
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadStateLayout
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalPagerApi
@Composable
fun HomeScreen(controller: NavHostController, vm: HomeScreenVM = viewModel()) {
    var state by remember {
        mutableStateOf(LoadState.Content)
    }
//    state = when (vm.bannerNetState.value) {
//        NetState.Loading -> LoadState.Loading
//        NetState.Success -> LoadState.Content
//        is NetState.Error -> LoadState.Retry
//        else -> LoadState.Loading
//    }
    val bannerState by vm.bannerList.observeAsState()
    if (bannerState == null) {
        vm.getBanner()
    }
    val topArticleState by vm.topArticleList.observeAsState()
    if (topArticleState == null) {
        vm.getTopArticles()
    }
    val articleState by vm.articleList.observeAsState()
    // Todo: 状态改变是否会导致整个HomeScreen刷新？如果是，那么需要确保不能重复获取数据
    Log.e("TAG", "HomeScreen: articleState 1 is null = ${articleState == null}")
    if (articleState.isNullOrEmpty()) {
        vm.getArticles()
    }
    LoadStateLayout(
        state = state,
        retryOnClick = {
//            vm.getBanner()
//            vm.getTopArticles()
//            vm.getArticles()
        }
    ) {
        Scaffold {
//        Text(text = "api broken", modifier = Modifier.padding(32.dp))
            Column {
                HomeBanner(vm = vm, bannerState = bannerState)

                val homeListState = rememberLazyListState()
                LazyColumn(state = homeListState) {
                    topArticleState?.let { articles ->
                        if (articles.isNotEmpty()) {
                            stickyHeader(key = 1) {
                                HomeListHeader("置顶文章")
                            }
                            items(articles.size) { item ->
                                ArticleItem(
                                    data = articles[item],
                                    isTop = true,
                                    onItemClick = {
                                        toDetail(
                                            controller = controller,
                                            url = it.link
                                        )
                                    }
                                )
                            }
                        }
                    }

                    articleState?.let { articles ->
                        if (articles.isNotEmpty()) {
                            stickyHeader(key = 2) {
                                HomeListHeader("最新文章")
                            }
                            items(articles.size) { item ->
                                ArticleItem(
                                    data = articles[item],
                                    onItemClick = {
                                        toDetail(
                                            controller = controller,
                                            url = it.link
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


fun toDetail(controller: NavHostController, url: String?) {
    controller.navigate("detail/$url")
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
private fun ArticleItem(
    data: ArticleData,
    onItemClick: (ArticleData) -> Unit = {},
    isTop: Boolean = false
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onItemClick(data) }
            .fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row {
                data.fresh?.let {
                    ArticleTag(text = "新", color = Color.Red, MaterialTheme.typography.caption)
                }
                Text(
                    text = data.title.orEmpty(),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                if (isTop) {
                    ArticleTag("置顶", Color.Red)
                }
                data.tags?.forEach {
                    ArticleTag(it.name, Color(0xFF009a61))
                }
                data.superChapterName?.let {
                    Text(
                        text = "分类: $it / ${data.chapterName}",
                        style = MaterialTheme.typography.caption
                    )
                }
            }
            Row {
                data.author?.let {
                    Text(
                        text = if (it.isNotEmpty()) "作者: $it" else "分享人: ${data.shareUser}",
                        style = MaterialTheme.typography.caption
                    )
                }
                data.niceDate?.let {
                    Text(
                        text = "时间: $it",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleTag(
    text: String,
    color: Color,
    textStyle: TextStyle = MaterialTheme.typography.caption
) {
    Text(
        text = text,
        color = color,
        style = textStyle,
        modifier = Modifier
            .padding(end = 10.dp)
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 2.dp)
    )
}

@ExperimentalPagerApi
@Composable
private fun HomeBanner(vm: HomeScreenVM, bannerState: List<HomeBannerData>?) {
    val bannerSize = bannerState?.size ?: 0
    if (bannerSize > 0 && bannerState != null) {
        // 使用 Int.MAX_VALUE 会出错
        val pagerState = rememberPagerState(
            pageCount = bannerSize * HomeBannerMaxCount,
            initialPage = bannerSize * HomeBannerMaxCount / 2
        )

        LaunchedEffect(key1 = 1) {
            while (true) {
                delay(HomeBannerInterval)
                var next = pagerState.currentPage
                next = if (next + 1 > pagerState.pageCount) 0 else next + 1
                pagerState.animateScrollToPage(next)
            }
        }

        BoxWithConstraints {
            HorizontalPager(
                state = pagerState,
                offscreenLimit = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(this.maxWidth.times(HomeBannerAspectRatio))
            ) { page ->
                HomeBannerItem(item = bannerState!!.get(page % bannerSize))
            }
        }
    }
}

@Composable
private fun HomeBannerItem(item: HomeBannerData) {
    val completed = remember { mutableStateOf(false) }
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (image, title) = createRefs()
        CoilImage(
            data = item.imagePath,
            contentDescription = item.title,
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                },
            onRequestCompleted = {
                completed.value = true
            },
            contentScale = ContentScale.Crop
        )
        if (completed.value) {
            Text(
                text = item.title,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(title) {
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .background(color = Color(0, 0, 0, 0xaa))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
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