package com.example.niumo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.niumo.R
import com.example.niumo.adapter.ArticleAdapter
import com.example.niumo.databinding.ActivityMainBinding
import com.example.niumo.api.article.GetArticleListService
import com.example.niumo.myclass.dataclass.Article
import com.example.niumo.myclass.helper.GetArticleInfoHelper
import com.example.niumo.myclass.dataclass.Msg4
import com.example.niumo.service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var articleAdapter: ArticleAdapter
    private var articleList = mutableListOf<Article>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articleAdapter = ArticleAdapter()
        articleList = loadArticle("listByClicks")

        articleAdapter.setData(articleList)
        binding.articleRecyclerView.adapter = articleAdapter

        //为每篇文章设置点击事件，点击进入文章详情页
        articleAdapter.setOnItemClickListener(object : ArticleAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, ArticleActivity::class.java)
                intent.putExtra("id", articleList[position].id)
                startActivity(intent)
            }
        })

        //界面跳转
         /*写文章*/
        binding.write.setOnClickListener {
            val intent = Intent(this@MainActivity, WriteActivity::class.java)
            startActivity(intent)
        }
         /*个人主页*/
        binding.my.setOnClickListener {
            val intent = Intent(this@MainActivity, MyActivity::class.java)
            startActivity(intent)
        }

        //返回按钮
        binding.back.setOnClickListener {
            finish()
        }

        /*
            为文章榜设置点击事件，改变文字颜色并加载文章，同MyActivity
            当前选中的榜会变为蓝色
            进入时默认为选中 我写的文章
         */
        binding.clickList.setOnClickListener {
            binding.clickList.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.blue_1))
            binding.latestList.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
            articleList = loadArticle("listByClicks")
        }

        binding.latestList.setOnClickListener {
            binding.latestList.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.blue_1))
            binding.clickList.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
            articleList = loadArticle("listByTime")
        }
    }

    /*
        加载文章，与MyActivity中的相同
        传入的参数为加载的文章的类型
        根据传入的参数，调用不同的接口
        先调用文章榜的接口，获取到文章id
        然后根据文章id调用获取文章信息的接口
        最后将获取到的文章作为MutableList<Article>返回
     */
    fun loadArticle(list: String): MutableList<Article> {
        var articleList = mutableListOf<Article>()
        val appService = ServiceCreator.create<GetArticleListService>()
        appService.getArticleList(list).enqueue(object : Callback<Msg4> {
            override fun onResponse(call: Call<Msg4>,
                                    response: Response<Msg4>
            ) {
                if (response.isSuccessful) {
                    //成功响应
                    val body = response.body()
                    if (body?.data != null) {
                        var t = 0
                        for (i in body.data) {
                            articleList.add(Article(i, "", "", -1, "", "", -1, "", "", -1, "", -1))
                        }
                        for (i in body.data) {
                            t++
                            var articleTemp = Article(-1, "", "", -1, "", "", -1, "", "", -1, "", -1)
                            GetArticleInfoHelper().fetchArticleInfo(articleTemp, i) { isSuccess ->
                                if (isSuccess) {
                                    //请求成功
                                    for (i in 0 until articleList.size) {
                                        if (articleList[i].id == articleTemp.id) {
                                            articleList[i] = articleTemp
                                        }
                                    }
                                    if (t == body.data.size) { //所有文章加载完毕
                                        articleAdapter.setData(articleList)
                                        binding.articleRecyclerView.adapter = articleAdapter
                                    }
                                } else {
                                    //请求失败
                                    Toast.makeText(this@MainActivity, "${t}号文章获取失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "文章获取失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Msg4>, t: Throwable) {
                //网络请求失败
                Toast.makeText(this@MainActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
            }
        })

        return articleList
    }
}