package com.example.niumo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.niumo.databinding.CommentBinding
import com.example.niumo.myclass.dataclass.fatherComment

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    private var data = mutableListOf<fatherComment>()
    private var itemClickListener: CommentAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val binding = CommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setData(fatherComment: MutableList<fatherComment>) {
        this.data = fatherComment
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val comment = data[position]
        holder.bind(comment)
        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(position) }
    }

    inner class ViewHolder(private val binding: CommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //为item设置内容
        fun bind(fatherComment: fatherComment) {
            binding.commentInfo.text = fatherComment.content
            binding.name.text = fatherComment.username
            Glide.with(binding.userAvatar.context)
                .load(fatherComment.avatar)
                .override(100, 100)
                .into(binding.userAvatar)
            val sonCommentRecyclerView = binding.sonCommentRecyclerView
            val sonCommentAdapter = SonCommentAdapter()

            sonCommentRecyclerView.adapter = sonCommentAdapter
            sonCommentAdapter.setData(fatherComment.childComment)
        }
    }
}

