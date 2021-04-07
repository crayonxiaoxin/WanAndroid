package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Subject
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun HomeScreen(controller: NavHostController) {
    val selectedTab = remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primarySurface)
    ) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                TopAppBar(title = { Text("HomeScreen") })
            },
            bottomBar = {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primarySurface
                ) {
                    BottomNavigationItem(
                        selected = selectedTab.value == 0,
                        onClick = { selectedTab.value = 0 },
                        icon = {
                            Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                        },
                        label = {
                            Text(text = "首页")
                        }
                    )
                    BottomNavigationItem(
                        selected = selectedTab.value == 1,
                        onClick = { selectedTab.value = 1 },
                        icon = {
                            Icon(imageVector = Icons.Filled.Subject, contentDescription = "Home")
                        },
                        label = {
                            Text(text = "体系")
                        }
                    )
                    BottomNavigationItem(
                        selected = selectedTab.value == 2,
                        onClick = { selectedTab.value = 2 },
                        icon = {
                            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Home")
                        },
                        label = {
                            Text(text = "导航")
                        }
                    )
                    BottomNavigationItem(
                        selected = selectedTab.value == 3,
                        onClick = { selectedTab.value = 3 },
                        icon = {
                            Icon(imageVector = Icons.Filled.Face, contentDescription = "Home")
                        },
                        label = {
                            Text(text = "我的")
                        }
                    )
                }
            }
        ) {
            Button(onClick = { controller.navigateUp() }) {
                Text("HomeScreen to LoginScreen")
            }


        }
    }


}