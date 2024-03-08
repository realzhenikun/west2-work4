package com.example.niumo.api.article

import com.example.niumo.myclass.dataclass.Msg4
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetArticleListService {
    @GET("article/{list}")
    fun getArticleList(@Path("list") list: String): Call<Msg4>
}