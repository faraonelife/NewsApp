package com.example.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.example.newsapp.util.NewsApplication
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class NewsViewModel(
   app:Application,
    val newsRepository: NewsRepository
): AndroidViewModel(app) {

     val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
     var breakingNewsPage = 1
    var breakingNewsResponse:NewsResponse?=null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
     var serchingNewsPage = 1
    var searchNewsResponse:NewsResponse?=null

    init { getBreakingNews("the-wall-street-journal")
    }
    fun getBreakingNews(source: String) = viewModelScope.launch {
        safeBreakingNewsCall(source)

    }

    fun searchNews(searchQuery:String)=viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }



    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticles=breakingNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                serchingNewsPage++
                if (searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }else{
                    val oldArticles=searchNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getSavedNews()=newsRepository.getSavedNews()
    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)

    }


    private suspend fun safeSearchNewsCall(serchQuery: String){
        searchNews.postValue(Resource.Loading)
        try {
            if (hasInternetConnection()){
                val response = newsRepository.searchNews(serchQuery, serchingNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }
            else{
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException->searchNews.postValue(Resource.Error("Network Failure"))
                else->searchNews.postValue(Resource.Error("Conversion Error"))
            }

        }

    }



    private suspend fun safeBreakingNewsCall(source: String){
        breakingNews.postValue(Resource.Loading)
        try {
            if (hasInternetConnection()){
            val response = newsRepository.getBreakingNews(source, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }
            else{
                breakingNews.postValue(Resource.Error("No internet connection"))

            }
        }
        catch (t:Throwable){
            when(t){
                is IOException->breakingNews.postValue(Resource.Error("Network Failure"))
                else->breakingNews.postValue(Resource.Error("Conversion Error"))
            }

        }

    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected &&
                    (networkInfo.type == ConnectivityManager.TYPE_WIFI ||
                            networkInfo.type == ConnectivityManager.TYPE_MOBILE ||
                            networkInfo.type == ConnectivityManager.TYPE_ETHERNET)
        }
    }

}
