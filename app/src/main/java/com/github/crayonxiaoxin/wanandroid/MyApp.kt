package com.github.crayonxiaoxin.wanandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.core.view.ViewCompat

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}

/**
 * 全局 toast
 */
fun toast(string: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApp.appContext, string, duration).show()
}

/**
 * 设置 状态栏 icon 颜色
 */
fun Activity.setSystemBarsDarkIcons(darkIcons: Boolean = false) {
    ViewCompat.getWindowInsetsController(this.window.decorView)?.let {
        it.isAppearanceLightStatusBars = darkIcons
    }
}