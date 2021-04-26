package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.github.crayonxiaoxin.wanandroid.model.BannerData
import com.google.accompanist.coil.CoilImage
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.isFinalState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

@ExperimentalPagerApi
@Composable
fun Banner(
    bannerSize: Int = 0,
    bannerItem: (currentPage: Int) -> BannerData,
    onItemClick: (BannerData) -> Unit = {},
    isAutoPlay: Boolean = true,
    offscreenLimit: Int = 2,
    maxCount: Int = 10000,
    aspectRatio: Float = 1.8f,
    autoInterval: Long = 3000L
) {
    if (bannerSize > 0) {
        // 使用 Int.MAX_VALUE 会出错
        val pagerState = rememberPagerState(
            pageCount = bannerSize * maxCount,
            initialPage = bannerSize * maxCount / 2
        )

        LaunchedEffect(key1 = "bannerAutoPlay") {
            while (isAutoPlay) {
                delay(autoInterval)
                var next = pagerState.currentPage
                next = if (next + 1 > pagerState.pageCount) 0 else next + 1
                pagerState.animateScrollToPage(next)
            }
        }

        HorizontalPager(
            state = pagerState,
            offscreenLimit = offscreenLimit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio) // w:h = aspectRatio (h = w/aspectRatio)
        ) { page ->
            BannerItem(
                item = bannerItem(page % bannerSize),
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun BannerItem(
    item: BannerData,
    onItemClick: (BannerData) -> Unit = {}
) {
    val completed = remember { mutableStateOf(false) }
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .clickable { onItemClick(item) }) {
        val (image, title) = createRefs()
        val painter = rememberCoilPainter(
            request = item.imagePath,
        )
        LaunchedEffect(painter) {
            snapshotFlow { painter.loadState }
                .filter { it.isFinalState() }
                .collect { completed.value = true }
        }
        Image(
            painter = rememberCoilPainter(
                request = item.imagePath,
            ),
            contentDescription = item.title,
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth(),
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