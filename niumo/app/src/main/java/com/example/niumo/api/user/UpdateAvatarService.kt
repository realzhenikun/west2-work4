package com.example.niumo.api.user

import com.example.niumo.myclass.dataclass.Avatar
import com.example.niumo.myclass.dataclass.Msg0
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface UpdateAvatarService {
    @PUT("user/updateAvatar")
    fun update(@Body data: Avatar): Call<Msg0>
}