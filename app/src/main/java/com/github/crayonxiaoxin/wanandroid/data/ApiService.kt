package com.github.crayonxiaoxin.wanandroid.data

import android.os.Build
import com.github.crayonxiaoxin.wanandroid.BuildConfig
import com.github.crayonxiaoxin.wanandroid.model.HomeBannerModel
import com.github.crayonxiaoxin.wanandroid.model.TopArticle
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("banner/json")
    suspend fun getHomeBanner(): HomeBannerModel

    @GET("article/top/json")
    suspend fun getTopArticles(): TopArticle

    companion object {
        val BASE_URL = "https://www.wanandroid.com/"
        operator fun invoke(): ApiService {
            val client = OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }.build()
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(ApiService::class.java)
        }
    }
}