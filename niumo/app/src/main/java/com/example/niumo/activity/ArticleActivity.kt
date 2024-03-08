package com.example.niumo.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.example.niumo.R
import com.example.niumo.adapter.CommentAdapter
import com.example.niumo.databinding.ArticleDetailBinding
import com.example.niumo.api.article.CommentOneService
import com.example.niumo.api.article.CommentTwoService
import com.example.niumo.api.article.GetCommentService
import com.example.niumo.api.user.GetUserArticleListService
import com.example.niumo.api.article.LikeArticleService
import com.example.niumo.myclass.dataclass.Article
import com.example.niumo.myclass.dataclass.Content
import com.example.niumo.myclass.helper.GetArticleInfoHelper
import com.example.niumo.myclass.dataclass.Msg0
import com.example.niumo.myclass.dataclass.Msg1
import com.example.niumo.myclass.dataclass.Msg4
import com.example.niumo.myclass.dataclass.fatherComment
import com.example.niumo.service.ServiceCreator
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class ArticleActivity : ComponentActivity() {
    private val NO_VALUE = -1
    private lateinit var binding: ArticleDetailBinding
    private lateinit var markwon: Markwon
    private val articleLoadingFinished = AtomicBoolean(false)
    private var isLike = false
    private var isClick = false
    private var isComment = false
    private var isCollect = false
    private var articleId: Int = NO_VALUE
    private lateinit var commentAdapter: CommentAdapter
    private var commentId = NO_VALUE
    private var commentType = ""
    private var fatherCommentList = mutableListOf<fatherComment>()
    private var articleTemp = Article(NO_VALUE, "", "", NO_VALUE, "", "", NO_VALUE, "", "", NO_VALUE, "", NO_VALUE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        commentAdapter = CommentAdapter()
        articleId = intent.getIntExtra("id", NO_VALUE)

        refreshComment()

        //为评论设置点击事件，点击评论后可以选中评论，对其进行回复
        commentAdapter.setOnItemClickListener(object : CommentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                binding.editText.hint = "回复${fatherCommentList[position].username}："
                commentId = fatherCommentList[position].id
                commentType = "fatherComment"
            }
        })

        //进入时默认隐藏评论区和发送按钮
        binding.commentRecyclerView.visibility = View.GONE
        binding.send.visibility = View.GONE

        //构建Markwon对象
        markwon = Markwon.builder(this)
            .build()

        //获取文章信息
        GetArticleInfoHelper().fetchArticleInfo(articleTemp, articleId) { isSuccess ->
            if (isSuccess) {
                //请求成功
                binding.title.text = articleTemp.title
                binding.authorName.text = articleTemp.authorName
                binding.datetime.text = articleTemp.datetime
                binding.likes.text = articleTemp.likes.toString()
                binding.comments.text = articleTemp.comment.toString()
                markwon.setMarkdown(binding.content, articleTemp.markdown)
                Glide.with(this@ArticleActivity)
                    .load(articleTemp.authorAvatar)
                    .override(100, 100)
                    .into(binding.authorAvatar)
                binding.clicks.text = articleTemp.clicks.toString()
                articleLoadingFinished.set(true)
            } else {
                Toast.makeText(this@ArticleActivity, "文章加载失败", Toast.LENGTH_SHORT).show()
            }
        }

        //异步加载文章数据
        GlobalScope.launch(Dispatchers.Main) {
            while (!articleLoadingFinished.get()) {
                delay(100)
            }
        }

        //返回按钮
        binding.back.setOnClickListener {
            finish()
        }

        //收藏按钮，但是没有接口
        binding.imageButton6.setOnClickListener {
            if (!isCollect) {
                Glide.with(this)
                    .load(R.drawable.img_5)
                    .into(binding.imageButton6)
                isCollect = true
            } else {
                Glide.with(this)
                    .load(R.drawable.img_4)
                    .into(binding.imageButton6)
                isCollect = false
            }
        }

        //点赞按钮
        binding.imageButton5.setOnClickListener {
            isClick = true
            if (!isLike) {
                Glide.with(this)
                    .load(R.drawable.img_7)
                    .into(binding.imageButton5)
                isLike = true
            } else {
                Glide.with(this)
                    .load(R.drawable.img_2)
                    .into(binding.imageButton5)
                isLike = false
            }
        }

        /*
            评论区，点击后隐藏文章内容显示评论区并默认选中文章
            此时直接评论会对文章进行评论
         */
        binding.imageButton.setOnClickListener {
            if (!isComment) {
                binding.editText.hint = "写评论..."
                commentId = articleId
                commentType = "article"
                binding.send.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
                binding.commentRecyclerView.visibility = View.VISIBLE
                isComment = true
            } else {
                binding.editText.hint = "善语结善缘，恶言伤人心"
                commentId = NO_VALUE
                commentType = ""
                binding.send.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
                binding.commentRecyclerView.visibility = View.GONE
                isComment = false
            }
        }

        //点击评论框会直接显示评论区并选中文章
        binding.editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.scrollView.visibility = View.GONE
                binding.commentRecyclerView.visibility = View.VISIBLE
                isComment = true
            }
        }
        /*
            发送按钮
            会根据当前选中的对象不同调用不同的接口
            选中文章时会对文章进行评论
            选中评论时会对评论进行评论
         */
        binding.send.setOnClickListener {
            if (commentId != NO_VALUE) {
                when (commentType) {
                    "article" -> {
                        val comment = binding.editText.text.toString()
                        if (comment.isNotEmpty()) {
                            val commentService = ServiceCreator.create<CommentOneService>()
                            commentService.addComment(commentId, Content(comment)).enqueue(object : Callback<Msg0> {
                                override fun onResponse(call: Call<Msg0>,
                                                        response: Response<Msg0>
                                ) {
                                    if (response.isSuccessful) {
                                        //成功响应
                                        refreshComment()
                                    }
                                }
                                override fun onFailure(call: Call<Msg0>, t: Throwable) {
                                    //网络请求失败
                                    Toast.makeText(this@ArticleActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                    "fatherComment" -> {
                        val comment = binding.editText.text.toString()
                        if (comment.isNotEmpty()) {
                            val commentService = ServiceCreator.create<CommentTwoService>()
                            commentService.addComment(commentId, Content(comment)).enqueue(object : Callback<Msg0> {
                                override fun onResponse(call: Call<Msg0>,
                                                        response: Response<Msg0>
                                ) {
                                    if (response.isSuccessful) {
                                        //成功响应
                                        refreshComment()
                                    }
                                }
                                override fun onFailure(call: Call<Msg0>, t: Throwable) {
                                    //网络请求失败
                                    Toast.makeText(this@ArticleActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }
            }
        }

        //获取用户赞过的文章，判断本文章用户是否赞过
        val appService = ServiceCreator.create<GetUserArticleListService>()
        appService.getUserArticleList("likes").enqueue(object : Callback<Msg4> {
            override fun onResponse(call: Call<Msg4>,
                                    response: Response<Msg4>
            ) {
                if (response.isSuccessful) {
                    //成功响应
                    val body = response.body()
                    if (body?.data != null) {
                        for (i in body.data) {
                            if (i == articleId) {
                                Glide.with(this@ArticleActivity)
                                    .load(R.drawable.img_7) // 图片A
                                    .into(binding.imageButton5)
                                isLike = true
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@ArticleActivity, "文章获取失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Msg4>, t: Throwable) {
                //网络请求失败
                Toast.makeText(this@ArticleActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //刷新评论
    fun refreshComment() {
        val appService1 = ServiceCreator.create<GetCommentService>()
        appService1.getComment(articleId).enqueue(object : Callback<Msg1> {
            override fun onResponse(call: Call<Msg1>,
                                    response: Response<Msg1>
            ) {
                if (response.isSuccessful) {
                    //成功响应
                    val body = response.body()
                    if (body != null) {
                        if (body.data.isNotEmpty()) {
                            //为item设置内容
                            fatherCommentList = body.data.toMutableList()
                            commentAdapter.setData(fatherCommentList)
                            binding.commentRecyclerView.adapter = commentAdapter
                        } else {
                            Toast.makeText(this@ArticleActivity, "暂无评论", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ArticleActivity, "评论获取失败", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ArticleActivity, "评论获取失败", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Msg1>, t: Throwable) {
                //网络请求失败
                Toast.makeText(this@ArticleActivity, "网络连接失败", Toast.LENGTH_SHORT).show()

            }
        })
    }

    /*
        关闭时判断是否进行过点赞操作（如果进入点过赞的文章，则会显示已赞，此时不算作进行过点赞操作）
        如果进行了点赞，则调用点赞文章接口
     */
    override fun onDestroy() {
        super.onDestroy()
        if (isLike && isClick) {
            val appService = ServiceCreator.create<LikeArticleService>()
            appService.likeArticle(articleId).enqueue(object : Callback<Msg0> {
                override fun onResponse(call: Call<Msg0>,
                                        response: Response<Msg0>
                ) {
                    if (!response.isSuccessful) {
                        //处理失败
                        Toast.makeText(this@ArticleActivity, "点赞失败", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Msg0>, t: Throwable) {
                    //网络请求失败
                    Toast.makeText(this@ArticleActivity, "网络连接失败", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}