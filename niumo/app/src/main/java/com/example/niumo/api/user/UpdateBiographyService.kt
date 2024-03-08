package com.example.niumo.api.user

import com.example.niumo.myclass.dataclass.Biography
import com.example.niumo.myclass.dataclass.Msg0
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface UpdateBiographyService {
    @PUT("user/biography")
    fun update(@Body data: Biography): Call<Msg0>
}