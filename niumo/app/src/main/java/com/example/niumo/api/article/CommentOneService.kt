package com.example.niumo.api.article

import com.example.niumo.myclass.dataclass.Content
import com.example.niumo.myclass.dataclass.Msg0
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentOneService {
    @POST("article/{id}/comment")
    fun addComment(@Path("id") id: Int,@Body content: Content): Call<Msg0>
}