package com.example.niumo.api.user

import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("user/login")
    fun login(@Body data: User): Call<Msg0>
}