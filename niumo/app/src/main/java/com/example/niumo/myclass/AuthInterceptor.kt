package com.example.niumo.myclass

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val mytoken: String) : Interceptor {
    //构建拦截器,为每个请求添加token
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "$mytoken")
            .build()

        return chain.proceed(request)
    }
}