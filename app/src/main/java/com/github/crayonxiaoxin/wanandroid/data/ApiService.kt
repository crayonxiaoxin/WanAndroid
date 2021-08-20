package com.github.crayonxiaoxin.wanandroid.data

import com.github.crayonxiaoxin.wanandroid.BuildConfig
import com.github.crayonxiaoxin.wanandroid.model.Articles
import com.github.crayonxiaoxin.wanandroid.model.BannerModel
import com.github.crayonxiaoxin.wanandroid.model.TopArticle
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("banner/json")
    suspend fun getHomeBanner(): BannerModel

    @GET("article/top/json")
    suspend fun getTopArticles(): TopArticle

    @GET("article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int = 1): Articles

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
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create()) // 防止提交数据时， string => "string"
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}