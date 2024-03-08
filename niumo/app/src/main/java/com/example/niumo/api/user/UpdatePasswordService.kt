package com.example.niumo.api.user

import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.Password
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface UpdatePasswordService {
    @PUT("user/password")
    fun update(@Body data: Password): Call<Msg0>
}