package com.github.crayonxiaoxin.wanandroid.ui.tixi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun TixiScreen(controller: NavHostController) {
    Scaffold(topBar = {
        TixiTopBar(
            background = MaterialTheme.colors.primary,
            selectedColor = Color.White
        )
    }) {
        Text(text = "tixi")
    }
}

@Composable
private fun TixiTopBar(
    background: Color = MaterialTheme.colors.background,
    selectedColor: Color = MaterialTheme.colors.primary,
    unSelectedColor: Color = Color.Gray
) {
    Column(modifier = Modifier.background(color = background)) {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            backgroundColor = background,
            elevation = 0.dp,
            contentPadding = PaddingValues(start = 0.dp, end = 0.dp)
        ) {
            val selectedIndex = remember {
                mutableStateOf(0)
            }
            // 并不能显示原始background
            TabRow(
                selectedTabIndex = selectedIndex.value,
                modifier = Modifier.fillMaxSize(),
                backgroundColor = background,
                indicator = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Transparent)
                    )
                }
            ) {
                Tab(
                    selected = selectedIndex.value == 0,
                    modifier = Modifier.fillMaxHeight(),
                    unselectedContentColor = unSelectedColor,
                    selectedContentColor = selectedColor,
                    onClick = { selectedIndex.value = 0 }) {
                    Text(text = "体系")
                }
                Tab(
                    selected = selectedIndex.value == 1,
                    modifier = Modifier.fillMaxHeight(),
                    unselectedContentColor = unSelectedColor,
                    selectedContentColor = selectedColor,
                    onClick = { selectedIndex.value = 1 }) {
                    Text(text = "导航")
                }
            }
        }
    }
}