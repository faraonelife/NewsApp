package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article

class NewsRepository (
    val db:ArticleDatabase
    ){
    suspend fun  getBreakingNews(source: String, pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(source,pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber:Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)
    suspend fun upsert(article: Article){
        val existArticle = db.getArticleDao().findByUrl(article.url)
        if (existArticle == null) {
            db.getArticleDao().upsert(article)
        } else {
            article.id = existArticle.id
            db.getArticleDao().upsert(article)
        }
    }
    fun getSavedNews()=db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticles(article)

}