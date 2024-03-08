package com.example.niumo.api.article

import com.example.niumo.myclass.dataclass.Msg0
import retrofit2.Call
import retrofit2.http.PUT
import retrofit2.http.Path

interface LikeArticleService {
    @PUT("article/{id}/likes")
    fun likeArticle(@Path("id") id: Int): Call<Msg0>
}