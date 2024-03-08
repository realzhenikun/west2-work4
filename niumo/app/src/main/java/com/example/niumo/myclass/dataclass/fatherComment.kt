package com.example.niumo.myclass.dataclass

data class fatherComment(var id: Int, var content: String, var userId: Int, var username: String, var avatar: String, var childComment: List<ChildComment>)
