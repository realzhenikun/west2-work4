package com.example.niumo.api.user

import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.Username
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface UpdateUsernameService {
    @PUT("user/username")
    fun update(@Body data: Username): Call<Msg0>
}