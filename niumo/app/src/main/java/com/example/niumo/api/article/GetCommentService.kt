package com.example.niumo.api.article

import com.example.niumo.myclass.dataclass.Msg1
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetCommentService {
    @GET("article/{id}/getComment")
    fun getComment(@Path("id") id: Int): Call<Msg1>
}