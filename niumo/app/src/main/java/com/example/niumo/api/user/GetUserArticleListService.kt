package com.example.niumo.api.user

import com.example.niumo.myclass.dataclass.Msg4
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetUserArticleListService {
    @GET("user/{list}")
    fun getUserArticleList(@Path("list") list: String): Call<Msg4>
}