package com.example.niumo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.example.niumo.databinding.ActivitySetBinding
import com.example.niumo.myclass.dataclass.UserInfo
import com.example.niumo.myclass.helper.GetUserInfoHelper

class SetActivity : ComponentActivity() {
    private lateinit var binding: ActivitySetBinding
    private var myInfo = UserInfo(0, "", "", "","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUI()

        setClickListener(binding.imageButton1, "avatar")
        setClickListener(binding.imageButton2, "username")
        setClickListener(binding.imageButton3, "password")
        setClickListener(binding.imageButton4, "biography")

        //返回按钮
        binding.back.setOnClickListener {
            finish()
        }
    }

    /*
        封装setOnClickListener
        根据点击的按钮不同向UpdateActivity传递不同的数据
     */
    private fun setClickListener(button: ImageButton, param: String) {
        button.setOnClickListener {
            val intent = Intent(this@SetActivity, UpdateActivity::class.java)
            intent.putExtra("update", param)
            startActivity(intent)
        }
    }

    //在Activity恢复时更新UI
    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        //更新个人信息
        GetUserInfoHelper().fetchUserInfo(myInfo) { isSuccess ->
            if (isSuccess) {
                binding.username.text = myInfo.username
                binding.password.text = myInfo.password
                Glide.with(this)
                    .load(myInfo.avatar)
                    .override(100, 100)
                    .into(binding.avatar)
                binding.biography.text = myInfo.biography
            } else {
                // 请求失败
                Toast.makeText(this, "个人信息获取失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}