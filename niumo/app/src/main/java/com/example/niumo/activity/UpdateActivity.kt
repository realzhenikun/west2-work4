package com.example.niumo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.niumo.databinding.ActivityUpdateBinding
import android.widget.Toast
import com.example.niumo.api.user.UpdateAvatarService
import com.example.niumo.api.user.UpdateBiographyService
import com.example.niumo.api.user.UpdatePasswordService
import com.example.niumo.api.user.UpdateUsernameService
import com.example.niumo.myclass.dataclass.Avatar
import com.example.niumo.myclass.dataclass.Biography
import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.Password
import com.example.niumo.myclass.dataclass.Username
import com.example.niumo.service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : ComponentActivity() {
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //接收SetActivity传入的数据
        val update = intent.getStringExtra("update").toString()

        //根据接收到的数据发送不同的请求
        binding.button.setOnClickListener {
            if (binding.editText.text.toString().isNotBlank()) {
                when (update) {
                    "biography" -> {
                        val appService = ServiceCreator.create<UpdateBiographyService>()
                        appService.update(Biography(binding.editText.text.toString())).enqueue(object : Callback<Msg0> {
                            override fun onResponse(call: Call<Msg0>,
                                                    response: Response<Msg0>
                            ) {
                                resp(response.isSuccessful)
                            }
                            override fun onFailure(call: Call<Msg0>, t: Throwable) {
                                Toast.makeText(this@UpdateActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    "username" -> {
                        val appService = ServiceCreator.create<UpdateUsernameService>()
                        appService.update(Username(binding.editText.text.toString())).enqueue(object : Callback<Msg0> {
                            override fun onResponse(call: Call<Msg0>,
                                                    response: Response<Msg0>
                            ) {
                                resp(response.isSuccessful)
                            }
                            override fun onFailure(call: Call<Msg0>, t: Throwable) {
                                Toast.makeText(this@UpdateActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    "password" -> {
                        val appService = ServiceCreator.create<UpdatePasswordService>()
                        appService.update(Password(binding.editText.text.toString())).enqueue(object : Callback<Msg0> {
                            override fun onResponse(call: Call<Msg0>,
                                                    response: Response<Msg0>
                            ) {
                                resp(response.isSuccessful)
                            }
                            override fun onFailure(call: Call<Msg0>, t: Throwable) {
                                Toast.makeText(this@UpdateActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    "avatar" -> {
                        val appService = ServiceCreator.create<UpdateAvatarService>()
                        appService.update(Avatar(binding.editText.text.toString())).enqueue(object : Callback<Msg0> {
                            override fun onResponse(call: Call<Msg0>,
                                                    response: Response<Msg0>
                            ) {
                                resp(response.isSuccessful)
                            }
                            override fun onFailure(call: Call<Msg0>, t: Throwable) {
                                Toast.makeText(this@UpdateActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            } else {
                Toast.makeText(this, "输入为空，请重新输入", Toast.LENGTH_SHORT).show()
            }
        }

        //返回按钮
        binding.back.setOnClickListener {
            finish()
        }
    }

    //返回响应
    fun resp(isSuccessful: Boolean) {
        //根据返回结果提示是否成功
        if (isSuccessful) {
            Toast.makeText(this@UpdateActivity, "修改成功", Toast.LENGTH_SHORT).show()
            //修改成功直接关闭界面，失败则不关闭
            finish()
        } else {
            Toast.makeText(this@UpdateActivity, "修改失败", Toast.LENGTH_SHORT).show()
        }
    }
}
