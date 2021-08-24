package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.crayonxiaoxin.wanandroid.data.LoadState


@Composable
fun LazyListFooter(
    loadState: LoadState,
    isTheEnd: Boolean = false,
    retry: () -> Unit = {},
    loadingLabel: String = "加载中...",
    retryLabel: String = "加载失败，请点击重试",
    theEndLabel: String = "已经到底了"
) {
    if (loadState == LoadState.Loading) {
        Text(
            text = loadingLabel,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth()
        )
    } else if (loadState is LoadState.Error) {
        Text(
            text = retryLabel,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth()
                .clickable {
                    retry()
                }
        )
    } else if (isTheEnd) {
        Text(
            text = theEndLabel,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth()
        )
    }
}
