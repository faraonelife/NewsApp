package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticlePreviewBinding

import com.example.newsapp.models.Article

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView.rootView) {

      var binding = ItemArticlePreviewBinding.bind(itemView)
        fun bind(article: Article) {
            binding.tvSource.text=article.source?.name
            binding.tvTitle.text=article.title
            binding.tvDescription.text=article.description
            binding.tvPublishedAt.text=article.publishedAt


        }
    }

    private val differCallBack=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
           return  oldItem==newItem
        }
    }

    val differ=AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false)
        )
    }
    private var onItemClickListener:((Article)->Unit)?=null
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
      val article=differ.currentList[position]
        holder.itemView.apply{
            Glide.with(this).load(article.urlToImage).into(holder.binding.ivArticleImage)
            setOnClickListener {
                onItemClickListener?.let { it(article) }

        }
        holder.bind(article)


        }

    }

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }

}