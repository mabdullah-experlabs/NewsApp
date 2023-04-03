package com.example.newsapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticlePreviewBinding
import com.example.newsapp.model.data_classes.Article


//not use diff utils
class NewsAdapter(private var list: List<Article>, private val listener: ((Article) -> Unit)? = null) :  RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currArticle = list[position]
        Glide.with(holder.itemView).load(currArticle.urlToImage).into(holder.binding.ivArticleImage)
        holder.binding.article = currArticle

        holder.itemView.setOnClickListener {
            listener?.invoke(currArticle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getArticle(position: Int) = list[position]
    fun updateList(articles: List<Article>) {
        list = articles
        notifyDataSetChanged()
    }
}