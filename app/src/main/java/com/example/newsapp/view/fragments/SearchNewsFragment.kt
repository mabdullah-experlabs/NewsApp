package com.example.newsapp.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.model.data_classes.Article
import com.example.newsapp.view.NewsActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.example.newsapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    // view binding for fragments
    private lateinit var binding: FragmentSearchNewsBinding

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        //adding delay for search
        var job: Job?=null
        binding.etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchNews.collectLatest { response->
                when(response){
                    is Resource.Success->{
                        hideProgressBar()
                        response.data?.let{
                            newsAdapter.updateList(it.articles)
                        }
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        response.message?.let{
                            Toast.makeText(activity,"An Error Occurred: $it",Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Loading->{
                        showProgressBar()
                    }
                }
            }
        }

    }

    private fun onItemClickListener(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(
            R.id.action_searchNewsFragment_to_articleFragment ,
            bundle
        )
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility =View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility =View.VISIBLE
    }
    private fun setupRecyclerView(){

        newsAdapter = NewsAdapter(listOf(),this ::onItemClickListener)
        binding.rvSearchNews.apply {
            binding.rvSearchNews.adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)

        }
    }

}