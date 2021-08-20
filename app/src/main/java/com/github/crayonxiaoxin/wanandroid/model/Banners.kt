package com.github.crayonxiaoxin.wanandroid.model

import com.github.crayonxiaoxin.wanandroid.ui.common.BannerData

data class BannerModel(
    val `data`: List<BannerData>,
    val errorCode: Int,
    val errorMsg: String
)
