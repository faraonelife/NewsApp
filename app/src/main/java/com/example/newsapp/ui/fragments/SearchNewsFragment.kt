package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment :Fragment(R.layout.fragment_search_news){
    lateinit var viewModel: NewsViewModel
  lateinit var binding:FragmentSearchNewsBinding
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                bundle)
        }
        var job: Job?=null
        binding.etSearch.addTextChangedListener{editable->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNewsResponse=null
                        viewModel.serchingNewsPage=1
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }



        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults/ Constants.QUERY_PAGE_SIZE
                        isLastPage=viewModel.serchingNewsPage==totalPages
                        if(isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                            Snackbar.make(view,"This is Last Page",Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"An error occurred , total load length  can be= 100: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()

                }
            }
        })
         }//onViewCreated
        private  fun hideProgressBar(){
          binding.paginationProgressBar.visibility=View.INVISIBLE
            isLoading=false
        }
        private  fun showProgressBar(){
           binding.paginationProgressBar.visibility=View.VISIBLE
            isLoading=true
        }


    var isLoading=false
    var isLastPage=false
    var isScrolling=false
    val scrollListener=object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount
            val isNotLoadingAndNotLastPage=!isLoading&&!isLastPage
            val isAtLastItem=firstVisibleItemPosition+visibleItemCount>=totalItemCount
            val isNotAtBeginning=firstVisibleItemPosition>=0
            val isTotalMoreThanVisible=totalItemCount>= Constants.QUERY_PAGE_SIZE
            val shouldPaginate=isNotLoadingAndNotLastPage && isAtLastItem&&isNotAtBeginning&&isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling=false
            }


        }
    }
        private fun setupRecyclerView(){
            newsAdapter= NewsAdapter()
            binding.rvSearchNews.apply{
                adapter=newsAdapter
                layoutManager= LinearLayoutManager(activity)
                addOnScrollListener(this@SearchNewsFragment.scrollListener)
            }
        }


    }
