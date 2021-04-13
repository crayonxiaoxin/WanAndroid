package com.github.crayonxiaoxin.wanandroid.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding


@Composable
fun DetailTopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "详情")
        }
    },
    navigationIcon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "",
            modifier = Modifier.clickable { navigationIconClick() }
        )
    },
    navigationIconClick: () -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        TopAppBar(
            modifier = Modifier
                .statusBarsPadding()
                .background(color = Color.Transparent),
        ) {
            Box {
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
}