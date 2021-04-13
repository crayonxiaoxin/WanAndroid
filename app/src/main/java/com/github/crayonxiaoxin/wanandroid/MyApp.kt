package com.github.crayonxiaoxin.wanandroid

import android.app.Application
import android.content.Context
import android.widget.Toast

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this.applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}

fun toast(string: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApp.appContext, string, duration).show()
}