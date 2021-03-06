package com.github.crayonxiaoxin.wanandroid.ui.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.toast
import com.github.crayonxiaoxin.wanandroid.ui.common.DetailTopBar
import com.github.crayonxiaoxin.wanandroid.ui.common.PageState
import com.github.crayonxiaoxin.wanandroid.ui.common.PageStateLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailScreen(controller: NavHostController, link: String) {
    Log.e("DetailScreen", "DetailScreen: link = $link")
    val state = remember {
        mutableStateOf<PageState>(PageState.Loading)
    }
    val onBack: () -> Unit = {
        CoroutineScope(Dispatchers.Main).launch {
            // 假裝耗時操作
            toast("back")
//            delay(1000)
            // 根據情況，是否返回
            controller.popBackStack()
        }
    }
    // 攔截返回事件
    BackHandler(enabled = true, onBack = onBack)
    Scaffold(
        topBar = {
            DetailTopBar(
                modifier = Modifier
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(bottomEnd = 30.dp))
                    .background(color = Color(255, 255, 255)),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 70.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "详情")
                    }
                },
                navigationIconClick = onBack
            )
        }
    ) {
        PageStateLayout(state = state.value, alwaysShowContent = true, retryOnClick = {}) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.run {
                            javaScriptEnabled = true
                            setSupportZoom(false)
                            javaScriptCanOpenWindowsAutomatically = false
                        }
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                url: String?
                            ): Boolean {
                                Log.e("DetailScreen", "shouldOverrideUrlLoading: $url")
                                return true
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                Log.e(
                                    "DetailScreen",
                                    "shouldOverrideUrlLoading: ${
                                        request?.url?.toString().orEmpty()
                                    }"
                                )
                                return true
                            }

                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                state.value = PageState.Loading
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                state.value = PageState.Content
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                errorCode: Int,
                                description: String?,
                                failingUrl: String?
                            ) {
                                super.onReceivedError(view, errorCode, description, failingUrl)
                                state.value = PageState.Retry
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                state.value = PageState.Retry
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {
                it.loadUrl(link)
            }
        }
    }
}
