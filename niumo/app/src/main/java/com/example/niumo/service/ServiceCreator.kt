package com.example.niumo.service

import com.example.niumo.App
import com.example.niumo.myclass.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private val appToken: String = App.token ?: ""
    // 创建OkHttpClient实例，并添加拦截器
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(appToken))
        .build()
    //构建Retrofit对象
    private const val BASE_URL = "http://47.113.146.130:8080/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

    fun getToken(): String {
        return App.token?: ""
    }
}