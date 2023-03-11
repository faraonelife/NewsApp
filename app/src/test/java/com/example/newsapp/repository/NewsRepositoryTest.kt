package com.example.newsapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapp.db.ArticleDao
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.Source
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NewsRepositoryTest{
    @Mock
    private lateinit var db: ArticleDatabase
    @Mock
    private lateinit var articleDao: ArticleDao

    private lateinit var repository: NewsRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = NewsRepository(db)
    }
    @Test
    fun `upsert should insert new article`() {
        runBlocking {
            val article = Article(1,
                "author",
                "content",
                "description",
                "publishedAt",
                Source("the-wall-street-journal","The Wall Street Journal"),
                "title",
                "url",
                "urlToImage"
            )

            `when`(db.getArticleDao()).thenReturn(articleDao)
            `when`(articleDao.findByUrl(article.url)).thenReturn(null)

            repository.upsert(article)

            verify(articleDao, times(1)).upsert(article)
        }
    }

}
