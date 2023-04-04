package com.example.newsapp.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.model.data_classes.Article
import com.example.newsapp.view.NewsActivity
import com.example.newsapp.view.adapters.NewsAdapter
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    // view binding for fragments
    private lateinit var binding: FragmentSavedNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedNewsBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        //setting delete article on swipe
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.getArticle(position)
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Article Successfully Deleted",Snackbar.LENGTH_LONG).apply{
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getSavedArticles().collectLatest {
                    newsAdapter.updateList(it)
                }
            }
        }


    }
    private fun onItemClickListener(article: Article) {

        val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article = article)
        findNavController().navigate(action)
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter(listOf(),this::onItemClickListener)
        binding.rvSavedNews.apply {

            adapter = newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}