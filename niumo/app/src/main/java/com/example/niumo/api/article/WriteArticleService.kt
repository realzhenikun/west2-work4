package com.example.niumo.api.article

import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.WriteArticleRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WriteArticleService {
    @POST("article/create")
    fun writeArticle(@Body data: WriteArticleRequest): Call<Msg0>
}