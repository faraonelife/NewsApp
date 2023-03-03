package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.models.Article
import com.example.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment :Fragment(R.layout.fragment_article){
     lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding
    lateinit var article: Article

    val args:ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding=FragmentArticleBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        article=args.article

        binding.webView.findViewById<WebView>(R.id.webView)
        binding.webView.apply{
            webViewClient= WebViewClient()
            loadUrl(article.url)
        }
        binding.fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article saved ;)",Snackbar.LENGTH_SHORT).show()
        }

    }
}