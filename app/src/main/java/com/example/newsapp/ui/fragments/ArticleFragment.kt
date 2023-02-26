package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.newsapp.NewsActivity
import com.example.newsapp.R
import com.example.newsapp.ui.NewsViewModel

class ArticleFragment :Fragment(R.layout.fragment_article){
    private lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel

    }
}