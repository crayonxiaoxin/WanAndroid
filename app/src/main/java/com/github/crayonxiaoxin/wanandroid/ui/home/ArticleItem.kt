package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.crayonxiaoxin.wanandroid.model.ArticleData
import com.github.crayonxiaoxin.wanandroid.ui.common.StrokeTag

@Composable
fun ArticleItem(
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
                    StrokeTag(text = "新", color = Color.Red, MaterialTheme.typography.caption)
                }
                Text(
                    text = data.title.orEmpty(),
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                if (isTop) {
                    StrokeTag("置顶", Color.Red)
                }
                data.tags?.forEach {
                    StrokeTag(it.name, Color(0xFF009a61))
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
