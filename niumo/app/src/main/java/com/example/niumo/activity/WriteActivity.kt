package com.example.niumo.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.niumo.databinding.ActivityWriteBinding
import com.example.niumo.api.article.WriteArticleService
import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.WriteArticleRequest
import com.example.niumo.service.ServiceCreator
import io.noties.markwon.Markwon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteActivity : ComponentActivity() {
    private lateinit var markwon: Markwon
    private lateinit var binding: ActivityWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //左上角返回按钮
        binding.back.setOnClickListener {
            finish()
        }

        //发布
        binding.post.setOnClickListener {
            //检测上传的内容是否为空
            if (binding.editTitle.text.isEmpty() || binding.editText.text.isEmpty()) {
                if (binding.editTitle.text.isEmpty()) {
                    Toast.makeText(this@WriteActivity, "请填写标题", Toast.LENGTH_SHORT).show()
                } else if (binding.editText.text.isEmpty()) {
                    Toast.makeText(this@WriteActivity, "请填写内容", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@WriteActivity, "发布失败", Toast.LENGTH_SHORT).show()
                }
            } else {
                val appService = ServiceCreator.create<WriteArticleService>()
                appService.writeArticle(WriteArticleRequest(binding.editTitle.text.toString(), binding.editText.text.toString())).enqueue(object : Callback<Msg0> {
                    override fun onResponse(call: Call<Msg0>,
                                            response: Response<Msg0>
                    ) {
                        if (response.isSuccessful) {
                            //成功响应
                            Toast.makeText(this@WriteActivity, "发布成功", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@WriteActivity, "发布失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Msg0>, t: Throwable) {
                        //网络请求失败
                        Toast.makeText(this@WriteActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        /*
            使用Markdown三方库Markwon: https://github.com/noties/Markwon实现Markdown编写和实时预览
         */

        //初始化 Markwon 实例
        markwon = Markwon.create(this@WriteActivity)

        //设置文本改变监听器
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                //在editText改变时，更新markdown预览
                updateMarkdownPreview(s.toString())
            }
        })
    }

    //更新markdown预览
    private fun updateMarkdownPreview(markdownText: String) {
        val renderedMarkdown = markwon.toMarkdown(markdownText)
        binding.textView.text = renderedMarkdown
    }
}