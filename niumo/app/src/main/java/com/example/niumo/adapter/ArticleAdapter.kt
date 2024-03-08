package com.example.niumo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.niumo.databinding.ArticleBinding
import com.example.niumo.myclass.dataclass.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    private var data = mutableListOf<Article>()
    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setData(articleList: MutableList<Article>) {
        this.data = articleList
        //刷新
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articleList = data[position]
        holder.bind(articleList)
        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(position) }
    }

    inner class ViewHolder(private val binding: ArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //为item设置内容
        fun bind(articleList: Article) {
            binding.articleTitle.text = articleList.title
            binding.likes.text = articleList.likes.toString()
            binding.comments.text = articleList.comment.toString()
            binding.articleContent.text = articleList.content
            Glide.with(binding.picture.context)
                .load(articleList.picture)
                .override(100, 100)
                .into(binding.picture)
        }
    }
}