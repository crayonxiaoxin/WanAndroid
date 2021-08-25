package com.github.crayonxiaoxin.wanandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.github.crayonxiaoxin.wanandroid.util.User

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
        User.provide(appContext)
    }

    companion object {
        lateinit var appContext: Context
    }
}

/**
 * 全局 toast
 */
fun toast(string: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(App.appContext, string, duration).show()
}

/**
 * 设置 状态栏 icon 颜色
 */
fun Activity.setSystemBarsDarkIcons(darkIcons: Boolean = false) {
    ViewCompat.getWindowInsetsController(this.window.decorView)?.let {
        it.isAppearanceLightStatusBars = darkIcons
    }
}

/**
 * 设置窗体透明度
 */
fun Activity.setWindowAlpha(alpha: Float, darkIcons: Boolean = false) {
    val layoutParams = window.attributes
    layoutParams.alpha = alpha
    window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    window.attributes = layoutParams
    setSystemBarsDarkIcons(darkIcons)
}

fun Context.setWindowAlpha(alpha: Float, darkIcons: Boolean = false) {
    (this as Activity).setWindowAlpha(alpha, darkIcons)
}