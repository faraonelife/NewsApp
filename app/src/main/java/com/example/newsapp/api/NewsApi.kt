package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNes(
        @Query("country")
        codeCountry: String="us",
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("query")
        searchQuery: String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsResponse>
}