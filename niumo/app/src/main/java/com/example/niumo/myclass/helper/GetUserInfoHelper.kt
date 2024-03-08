package com.example.niumo.myclass.helper

import com.example.niumo.api.user.GetUserInfoService
import com.example.niumo.myclass.dataclass.Msg2
import com.example.niumo.myclass.dataclass.UserInfo
import com.example.niumo.service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetUserInfoHelper {
    //获取用户信息
    fun fetchUserInfo(userInfo: UserInfo, callback: (Boolean) -> Unit) {
        val appService = ServiceCreator.create<GetUserInfoService>()
        appService.getUserInfo().enqueue(object : Callback<Msg2> {
            override fun onResponse(call: Call<Msg2>, response: Response<Msg2>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        //设置用户信息
                        userInfo.apply {
                            id = body.data.id
                            username = body.data.username
                            password = body.data.password
                            biography = body.data.biography
                            avatar = body.data.avatar
                        }
                    }
                    callback(true)
                } else {
                    callback(false)
                }
            }
            override fun onFailure(call: Call<Msg2>, t: Throwable) {
                callback(false)
            }
        })
    }
}