package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalCoilApi::class, ExperimentalPagerApi::class)
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
            initialPage = bannerSize * maxCount / 2,
            initialOffscreenLimit = offscreenLimit
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

@ExperimentalCoilApi
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
        val painter = rememberImagePainter(
            data = item.imagePath,
        )
//        LaunchedEffect(painter) {
//            snapshotFlow { painter.loadState }
//                .filter { it.isFinalState() }
//                .collect { completed.value = true }
//        }
        Image(
            painter = painter,
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

data class BannerData(
    val id: Int,
    val title: String,
    val imagePath: String,
    val url: String,
    val desc: String?,
    val isVisible: Int?,
    val order: Int?,
    val type: Int?
)