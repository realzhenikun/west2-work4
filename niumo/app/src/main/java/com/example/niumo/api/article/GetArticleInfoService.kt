package com.example.niumo.api.article

import com.example.niumo.myclass.dataclass.Msg3
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetArticleInfoService {
    @GET("article/{id}")
    fun getArticleInfo(@Path("id") id: Int): Call<Msg3>
}