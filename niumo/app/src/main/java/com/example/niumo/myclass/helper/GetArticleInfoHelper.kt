package com.example.niumo.myclass.helper

import com.example.niumo.api.article.GetArticleInfoService
import com.example.niumo.myclass.dataclass.Article
import com.example.niumo.myclass.dataclass.Msg3
import com.example.niumo.service.ServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetArticleInfoHelper {
    //获取文章信息
    fun fetchArticleInfo(articleInfo: Article, articleId: Int, callback: (Boolean) -> Unit) {
        val appService = ServiceCreator.create<GetArticleInfoService>()
        appService.getArticleInfo(articleId).enqueue(object : Callback<Msg3> {
            override fun onResponse(call: Call<Msg3>, response: Response<Msg3>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        //设置文章信息
                        articleInfo.apply {
                            id = body.data.id ?: articleId
                            title = body.data.title ?: "标题为空"
                            content = body.data.content ?: "内容为空"
                            picture = body.data.picture ?: "https://img0.baidu.com/it/u=1411984117,2841796844&fm=253&fmt=auto&app=138&f=JPEG?w=602&h=500"
                            datetime = body.data.datetime ?: "日期为空"
                            likes = body.data.likes ?: -1
                            authorName = body.data.authorName ?: "作者为空"
                            authorAvatar = body.data.authorAvatar ?: "https://img0.baidu.com/it/u=1411984117,2841796844&fm=253&fmt=auto&app=138&f=JPEG?w=602&h=500"
                            clicks = body.data.clicks ?: -1
                            markdown = body.data.markdown ?: "markdown为空"
                            comment = body.data.comment ?: -1
                        }
                    } else {
                        callback(false)
                    }
                    callback(true)
                } else {
                    callback(false)
                }
            }
            override fun onFailure(call: Call<Msg3>, t: Throwable) {
                callback(false)
            }
        })
    }
}