package com.github.crayonxiaoxin.wanandroid.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.data.Repository
import com.github.crayonxiaoxin.wanandroid.data.Result
import com.github.crayonxiaoxin.wanandroid.data.succeeded
import com.github.crayonxiaoxin.wanandroid.data.successOr
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(controller: NavHostController, viewModel: HomeScreenVM = viewModel()) {
    Scaffold(
        topBar = {
            HomeTopBar()
        }
    ) {
        val scope = rememberCoroutineScope()
        scope.launch {
//            viewModel.list.value.addAll(Repository.getHomeBanner().data)
            val res = Repository.getHomeBanner3()
            if (res.succeeded) {
                Log.e("TAG", "HomeScreen: " + (res as Result.Success).data.toString())
            } else {
                (res as Result.Error).exception
            }
        }
    }
}

@Composable
private fun HomeTopBar() {
    Box(Modifier.background(color = MaterialTheme.colors.primary)) {
        TopAppBar(
            modifier = Modifier
                .statusBarsPadding()
                .background(color = Color.Transparent)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "首頁")
            }
        }
    }
}