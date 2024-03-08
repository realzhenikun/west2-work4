package com.example.niumo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.niumo.R
import com.example.niumo.adapter.ArticleAdapter
import com.example.niumo.databinding.ActivityMyBinding
import com.example.niumo.api.user.GetUserArticleListService
import com.example.niumo.myclass.dataclass.Article
import com.example.niumo.myclass.helper.GetArticleInfoHelper
import com.example.niumo.myclass.dataclass.UserInfo
import com.example.niumo.myclass.helper.GetUserInfoHelper
import com.example.niumo.myclass.dataclass.Msg4
import com.example.niumo.service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class MyActivity : ComponentActivity() {
    private lateinit var binding: ActivityMyBinding
    private lateinit var articleAdapter: ArticleAdapter
    private var myInfo = UserInfo(0, "", "", "","")
    private var articleList = mutableListOf<Article>()
    private var articleLoadingFinished = AtomicBoolean(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        articleAdapter = ArticleAdapter()

        updateUI()

        articleList = loadArticle("getArticle")

        articleAdapter.setData(articleList)
        binding.articleRecyclerView.adapter = articleAdapter

        //为每篇文章设置点击事件，点击进入文章详情页
        articleAdapter.setOnItemClickListener(object : ArticleAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MyActivity, ArticleActivity::class.java)
                intent.putExtra("id", articleList[position].id)
                startActivity(intent)
            }
        })

        /*
            为文章榜设置点击事件，改变文字颜色并加载文章，同MainActivity
            当前选中的榜会变为蓝色
            进入时默认为选中 我写的文章
         */
        binding.myArticle.setOnClickListener {
            binding.myArticle.setTextColor(ContextCompat.getColor(this@MyActivity, R.color.blue_1))
            binding.likeArticle.setTextColor(ContextCompat.getColor(this@MyActivity, R.color.black))
            articleList = loadArticle("getArticle")
        }
        binding.likeArticle.setOnClickListener {
            binding.likeArticle.setTextColor(ContextCompat.getColor(this@MyActivity, R.color.blue_1))
            binding.myArticle.setTextColor(ContextCompat.getColor(this@MyActivity, R.color.black))
            articleList = loadArticle("likes")
        }

        //返回按钮
        binding.back.setOnClickListener {
            finish()
        }

        //编辑个人信息
        binding.button.setOnClickListener {
            val intent = Intent(this, SetActivity::class.java)
            startActivity(intent)
        }
    }

    //恢复时更新UI和文章数据
    override fun onResume() {
        super.onResume()
        updateUI()
        loadArticle("getArticle")
    }

    private fun updateUI() {
        //更新个人信息
        GetUserInfoHelper().fetchUserInfo(myInfo) { isSuccess ->
            if (isSuccess) {
                binding.textView3.text = myInfo.username
                Glide.with(this)
                    .load(myInfo.avatar)
                    .override(100, 100)
                    .into(binding.imageView3)
                if (myInfo.biography != "null") {
                    binding.biography.text = myInfo.biography
                }
                articleLoadingFinished.set(true)
            } else {
                // 请求失败
                Toast.makeText(this@MyActivity, "个人信息获取失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
        加载文章，与MainActivity中的相同
        传入的参数为加载的文章的类型
        根据传入的参数，调用不同的接口
        先调用文章榜的接口，获取到文章id
        然后根据文章id调用获取文章信息的接口
        最后将获取到的文章作为MutableList<Article>返回
     */
    fun loadArticle(list: String): MutableList<Article> {
        var articleList = mutableListOf<Article>()
        //调用文章榜接口
        val appService = ServiceCreator.create<GetUserArticleListService>()
        appService.getUserArticleList(list).enqueue(object : Callback<Msg4> {
            override fun onResponse(call: Call<Msg4>,
                                    response: Response<Msg4>
            ) {
                if (response.isSuccessful) {
                    //成功响应
                    val body = response.body()
                    if (body?.data != null) {
                        var t = 0
                        /*
                            文章榜返回的数据为文章id序列
                            先将文章id按顺序存入articleList中，然后再依次获取每篇文章的详细信息
                            *如果根据文章id序列依次获取每篇文章的信息，然后存入articleList中，会导致文章乱序
                         */
                        for (i in body.data) {
                            articleList.add(Article(i, "", "", -1, "", "", -1, "", "", -1, "", -1))
                        }
                        for (i in body.data) {
                            //用t控制循环次数
                            t++
                            var articleTemp = Article(-1, "", "", -1,"", "", -1, "", "", -1, "", -1)
                            //获取文章信息
                            GetArticleInfoHelper().fetchArticleInfo(articleTemp, i) { isSuccess ->
                                if (isSuccess) {
                                    //请求成功
                                    //将文章信息存入对应的articleList成员中
                                    for (i in 0 until articleList.size) {
                                        if (articleList[i].id == articleTemp.id) {
                                            articleList[i] = articleTemp
                                        }
                                    }
                                    if (t == body.data.size) { // 当所有文章加载完毕
                                        articleAdapter.setData(articleList)
                                        binding.articleRecyclerView.adapter = articleAdapter
                                    }
                                } else {
                                    //请求失败
                                    Toast.makeText(this@MyActivity, "${t}号文章获取失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MyActivity, "文章获取失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Msg4>, t: Throwable) {
                //网络请求失败
                Toast.makeText(this@MyActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
            }
        })

        return articleList
    }
}