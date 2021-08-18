package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun DetailTopBar(
    modifier: Modifier = Modifier,
    titleLabel: String = "",
    title: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = titleLabel)
        }
    },
    navigationIconVector: ImageVector = Icons.Default.ArrowBack,
    navigationIcon: @Composable () -> Unit = {
        Icon(
            imageVector = navigationIconVector,
            contentDescription = "",
            modifier = Modifier
                .clip(shape = RoundedCornerShape(30.dp)) // 5. 裁剪點擊效果
                .clickable { navigationIconClick() } // 4. 添加點擊效果
                .padding(10.dp) // 3. 在 寬30、高30 的基礎上增加 10 padding
                .height(30.dp)
                .width(30.dp)
        )
    },
    navigationIconClick: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .background(color = Color.Transparent)
                .height(60.dp)
        ) {
            title()
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationIcon()
            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                actions()
            }
        }
    }
}