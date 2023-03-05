package com.example.newsapp.db

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.*
import com.example.newsapp.models.Article
import com.example.newsapp.util.Constants.Companion.KEY_FIRST_TIME_TOGGLE
import com.example.newsapp.util.Constants.Companion.KEY_PASSWORD
import com.example.newsapp.util.Constants.Companion.KEY_USERNAME
import com.example.newsapp.util.Constants.Companion.SHARED_PREFERENCE_NAME

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)

abstract class ArticleDatabase : RoomDatabase(){
    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile
        private var instance:ArticleDatabase?=null
        private val LOCK=Any()

        operator fun invoke(context: Context)= instance?: synchronized(LOCK){
            instance?:createDatabase(context).also{ instance=it}
        }

    private  fun createDatabase(context: Context)=
        Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()



} }
