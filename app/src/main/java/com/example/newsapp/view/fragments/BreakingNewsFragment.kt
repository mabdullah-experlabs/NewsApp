package com.example.newsapp.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.model.data_classes.Article
import com.example.newsapp.utils.Resource
import com.example.newsapp.view.NewsActivity
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    // view binding for fragments
    private lateinit var binding: FragmentBreakingNewsBinding

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBreakingNewsBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel



        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.breakingNews.collectLatest { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let {
                            newsAdapter.updateList(it.articles)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let {
                            Toast.makeText(activity, "An Error Occurred: $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }

//        if (activity is ArticleInterface){
//            (activity as ArticleInterface).let {
//                articleInterface = it
//            }
//        }


    }

    private fun onItemClickListener(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }
        findNavController().navigate(
            R.id.action_breakingNewsFragment_to_articleFragment,
            bundle
        )
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(listOf(), this::onItemClickListener)
        binding.rvBreakingNews.apply {
            //send data to article fragment through Navigation
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

//    override fun onTitleReceived(title: String) {
//         Toast.makeText(requireContext(), "Article saved: $title", Toast.LENGTH_SHORT).show()
////        toast.setGravity(Gravity.TOP, 0, 0)
////        toast.show()
//        Log.i("Masla","----")
//    }

}