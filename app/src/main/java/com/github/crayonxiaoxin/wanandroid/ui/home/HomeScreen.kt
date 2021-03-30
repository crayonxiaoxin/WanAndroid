package com.github.crayonxiaoxin.wanandroid.ui.home

import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate

@Composable
fun HomeScreen(controller: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("HomeScreen") })
        }
    ) {
        Button(onClick = { controller.navigateUp() }) {
            Text("HomeScreen to LoginScreen")
        }

    }
}