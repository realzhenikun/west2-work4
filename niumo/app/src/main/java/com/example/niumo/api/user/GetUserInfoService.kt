package com.example.niumo.api.user

import com.example.niumo.myclass.dataclass.Msg2
import retrofit2.Call
import retrofit2.http.GET

interface GetUserInfoService {
    @GET("user/userInfo")
    fun getUserInfo(): Call<Msg2>
}