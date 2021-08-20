package com.github.crayonxiaoxin.wanandroid.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun getCurrentMills(): Long {
        return System.currentTimeMillis()
    }

    fun getCurrentDate(): Date {
        val c = Calendar.getInstance(Locale.CHINA)
        return c.time
    }

    fun getCurrentDateFormat(format: String = "yyyy-MM-dd"): String {
        val c = Calendar.getInstance(Locale.CHINA)
        val sdf = SimpleDateFormat(format, Locale.CHINA)
        return sdf.format(c.time)
    }

    fun getDateFormat(format: String = "yyyy-MM-dd", date: Date): String {
        val c = Calendar.getInstance(Locale.CHINA)
        c.time = date
        val sdf = SimpleDateFormat(format, Locale.CHINA)
        return sdf.format(c.time)
    }

    fun getDateByFormat(format: String = "yyyy-MM-dd", date: String): Date {
        val sdf = SimpleDateFormat(format, Locale.CHINA)
        return sdf.parse(date)!!
    }

    fun getMonthAgo(amount: Int = -1): Date {
        val c = Calendar.getInstance(Locale.CHINA)
        c.add(Calendar.MONTH, amount)
        return c.time
    }

    fun getDateFormatByYmd(y: Int, m: Int, d: Int, separator: String = "-"): String {
        return "${y}${separator}${String.format("%02d", m)}${separator}${String.format("%02d", d)}"
    }
}