package com.example.niumo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.niumo.App
import com.example.niumo.databinding.ActivityLoginBinding
import com.example.niumo.api.user.LoginService
import com.example.niumo.api.user.RegisterService
import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class LoginActivity : ComponentActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var user = User("", "")
    //注册和登录时不会带有token
    private val BASE_URL = "http://47.113.146.130:8080/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //注册成功提示，默认隐藏
        binding.tip.visibility = View.GONE

        //注册成功后选则是否登录
        binding.yes.setOnClickListener {
            binding.login.visibility = View.VISIBLE
            binding.tip.visibility = View.GONE
            login()
        }

        binding.no.setOnClickListener {
            binding.login.visibility = View.VISIBLE
            binding.tip.visibility = View.GONE
        }

        //登录按钮
        binding.button.setOnClickListener {
            login()
        }

        //注册按钮
        binding.button2.setOnClickListener {
            register()
        }
    }

    /*
        登录
        未输入内容不能登录
        没有注册的账号不能登录
        登录成功后会返回token
        后续所有接口调用都需要token
     */
    fun login() {
        user.username = binding.editUsername.text.toString()
        user.password = binding.editText1.text.toString()
        if (user.username == "") {
            Toast.makeText(this@LoginActivity, "用户名不能为空", Toast.LENGTH_SHORT).show()
        } else if (user.password == "") {
            Toast.makeText(this@LoginActivity, "密码不能为空", Toast.LENGTH_SHORT).show()
        } else {
            val appService = retrofit.create<LoginService>()
            appService.login(user).enqueue(object : Callback<Msg0> {
                override fun onResponse(call: Call<Msg0>,
                                        response: Response<Msg0>
                ) {
                    if (response.isSuccessful) {
                        //成功响应
                        val body = response.body()
                        App.token = body!!.data
                        if (body != null) {
                            if (body.msg == "success") {
                                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Msg0>, t: Throwable) {
                    //网络请求失败
                    Toast.makeText(this@LoginActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    /*
        用户注册
        注册时用户名和密码不能为空
     */
    fun register() {
        user.username = binding.editUsername.text.toString()
        user.password = binding.editText1.text.toString()
        if (user.username == "") {
            Toast.makeText(this@LoginActivity, "用户名不能为空", Toast.LENGTH_SHORT).show()
        } else if (user.password == "") {
            Toast.makeText(this@LoginActivity, "密码不能为空", Toast.LENGTH_SHORT).show()
        } else {
            val appService1 = retrofit.create<RegisterService>()
            appService1.register(user).enqueue(object : Callback<Msg0> {
                override fun onResponse(call: Call<Msg0>,
                                        response: Response<Msg0>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            if (body.msg == "SUCCESS") {
                                binding.login.visibility = View.GONE
                                binding.tip.visibility = View.VISIBLE
                            } else {
                                Toast.makeText(this@LoginActivity, "注册失败", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "注册失败", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Msg0>, t: Throwable) {
                    //网络请求失败
                    Toast.makeText(this@LoginActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}