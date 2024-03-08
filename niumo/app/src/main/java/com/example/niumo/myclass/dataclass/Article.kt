package com.example.niumo.myclass.dataclass

data class Article(
    var id: Int,
    var title: String,
    var content: String,
    var userId: Int,
    var picture: String,
    var datetime: String,
    var likes: Int,
    var authorName: String,
    var authorAvatar: String,
    var clicks: Int,
    var markdown: String,
    var comment: Int
)