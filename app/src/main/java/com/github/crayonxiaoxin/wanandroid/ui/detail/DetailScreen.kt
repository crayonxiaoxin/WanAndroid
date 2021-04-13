package com.github.crayonxiaoxin.wanandroid.ui.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.github.crayonxiaoxin.wanandroid.data.NetState
import com.github.crayonxiaoxin.wanandroid.toDetail
import com.github.crayonxiaoxin.wanandroid.toast
import com.github.crayonxiaoxin.wanandroid.ui.common.DetailTopBar
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadState
import com.github.crayonxiaoxin.wanandroid.ui.common.LoadStateLayout

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailScreen(controller: NavHostController, link: String) {
    Log.e("DetailScreen", "DetailScreen: link = $link")
    val state = remember {
        mutableStateOf(LoadState.Loading)
    }
    Scaffold(
        topBar = {
            DetailTopBar(
                modifier = Modifier.background(color = MaterialTheme.colors.primary),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "详情")
                    }
                },
                navigationIconClick = {
                    controller.popBackStack()
                }
            )
        }
    ) {
        LoadStateLayout(state = state.value, alwaysShowContent = true, retryOnClick = {}) {
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
//                                toDetail(controller = controller, url = url)
                                Log.e("DetailScreen", "shouldOverrideUrlLoading: $url")
                                return true
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
//                                toDetail(
//                                    controller = controller,
//                                    url = request?.url?.toString().orEmpty()
//                                )
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
                                state.value = LoadState.Loading
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                state.value = LoadState.Content
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                errorCode: Int,
                                description: String?,
                                failingUrl: String?
                            ) {
                                super.onReceivedError(view, errorCode, description, failingUrl)
                                state.value = LoadState.Retry
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                state.value = LoadState.Retry
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