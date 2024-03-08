package com.example.niumo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.niumo.databinding.SonCommentBinding
import com.example.niumo.myclass.dataclass.ChildComment

class SonCommentAdapter : RecyclerView.Adapter<SonCommentAdapter.ViewHolder>() {
    private var data = mutableListOf<ChildComment>()
    private var itemClickListener: SonCommentAdapter.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SonCommentAdapter.ViewHolder {
        val binding = SonCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setData(comment: List<ChildComment>) {
        this.data = comment.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SonCommentAdapter.ViewHolder, position: Int) {
        val comment = data[position]
        holder.bind(comment)
        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(position) }
    }
    inner class ViewHolder(private val binding: SonCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //为item设置内容
        fun bind(comment: ChildComment) {
            binding.commentInfo.text = comment.content
            binding.name.text = comment.username
            Glide.with(binding.userAvatar.context)
                .load(comment.avatar)
                .override(100, 100)
                .into(binding.userAvatar)
        }
    }
}