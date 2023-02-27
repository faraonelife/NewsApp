package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.ui.NewsViewModel

class SavedNewsFragment :Fragment(R.layout.fragment_saved_news){
    private lateinit var viewModel: NewsViewModel
    lateinit var binding:FragmentSavedNewsBinding
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSavedNewsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,
                bundle)
        }

    }
    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvSavedNews.apply{
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}