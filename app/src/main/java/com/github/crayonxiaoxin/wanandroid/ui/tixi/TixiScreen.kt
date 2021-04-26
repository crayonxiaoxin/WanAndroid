package com.github.crayonxiaoxin.wanandroid.ui.tixi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun TixiScreen(controller: NavHostController) {
    Scaffold(topBar = { DefaultTopBar(background = Color.White) }) {
        Text(text = "tixi")
    }
}

@Composable
private fun DefaultTopBar(background: Color = MaterialTheme.colors.background) {
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
            TabRow(
                selectedTabIndex = selectedIndex.value,
                modifier = Modifier.fillMaxSize(),
                backgroundColor = Color.White,
                contentColor = MaterialTheme.colors.primary
            ) {
                Tab(
                    selected = selectedIndex.value == 0,
                    modifier = Modifier.fillMaxHeight(),
                    onClick = { selectedIndex.value = 0 }) {
                    Text(text = "体系")
                }
                Tab(
                    selected = selectedIndex.value == 1,
                    modifier = Modifier.fillMaxHeight(),
                    onClick = { selectedIndex.value = 1 }) {
                    Text(text = "导航")
                }
            }
        }
    }
}