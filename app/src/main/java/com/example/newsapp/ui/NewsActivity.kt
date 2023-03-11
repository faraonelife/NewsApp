package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository


class NewsActivity : AppCompatActivity() {
    lateinit var binding :ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startService(Intent(applicationContext, Notification::class.java))
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory=NewsViewModelProviderFactory(application, newsRepository)
        viewModel =ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        val navHostFragment =supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        navHostFragment.findNavController()
            .addOnDestinationChangedListener{
                _,destination,_ ->
                when(destination.id){
                    R.id.breakingNewsFragment,
                    R.id.searchNewsFragment,
                    R.id.savedNewsFragment ->binding.bottomNavigationView.visibility =View.VISIBLE
                    else->binding.bottomNavigationView.visibility=View.GONE
                }

            }

    }




}