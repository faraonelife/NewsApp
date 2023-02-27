package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.util.Resource

class BreakingNewsFragment :Fragment(R.layout.fragment_breaking_news){
     lateinit var viewModel: NewsViewModel
     lateinit var newsAdapter: NewsAdapter
     lateinit var binding: FragmentBreakingNewsBinding
    val TAG="BreakingNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener{article->
            val bundle=Bundle().apply{
                putSerializable("article",article)
                Log.d(TAG,"success---$article")
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,
            bundle)
        }



        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{
                        newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {
                        message-> Log.e(TAG,"An Error occurred: $message")
                    }
                }
                is Resource.Loading->{
                    showProgressBar()

                }
            }
        })


    }
    private  fun hideProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
    }
    private  fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
    }
    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvBreakingNews.apply{
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }
}