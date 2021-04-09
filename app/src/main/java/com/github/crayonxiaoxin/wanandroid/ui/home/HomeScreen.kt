package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.model.HomeBannerData
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@ExperimentalPagerApi
@Composable
fun HomeScreen(controller: NavHostController, viewModel: HomeScreenVM = viewModel()) {
    Scaffold(
//        topBar = { HomeTopBar() }
    ) {
        viewModel.getBanner()
        viewModel.getTopArticles()
        LazyColumn() {
            item {
                HomeBanner(viewModel)
            }
            items(viewModel.articleSize) { item ->
                Text(text = viewModel.articleList.value[item].title)
            }
        }

    }
}

@ExperimentalPagerApi
@Composable
private fun HomeBanner(viewModel: HomeScreenVM) {
    val bannerSize = viewModel.bannerSize
    if (bannerSize > 0) {
        // 使用 Int.MAX_VALUE 会出错
        val pagerState = rememberPagerState(
            pageCount = bannerSize * 10000,
            initialPage = bannerSize * 5000
        )
        HorizontalPager(
            state = pagerState,
            offscreenLimit = 2,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            HomeBannerItem(item = viewModel.bannerList.value[page % bannerSize])
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