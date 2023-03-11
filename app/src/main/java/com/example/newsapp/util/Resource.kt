package com.example.newsapp.util

sealed class Resource<out T>{
    class Success<T>(val data: T):Resource<T>()
    class Error<T>(val message: String?):Resource<T>()
   object  Loading:Resource<Nothing>()
}