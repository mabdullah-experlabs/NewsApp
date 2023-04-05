package com.example.newsapp.view.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSharedNewsBinding
import com.example.newsapp.view.NewsActivity
import com.example.newsapp.viewmodel.NewsViewModel

class SharedNewsFragment :Fragment(R.layout.fragment_shared_news) {

    private var binding:FragmentSharedNewsBinding? = null
    private var viewModel:NewsViewModel?=null

    private val args : SharedNewsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSharedNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        val articleUrl = Uri.decode(args.articleUrl)
        Log.d("SharedNewsFragment", "ARGS Contain: $args")

        binding!!.sharedWebView.apply {
            webViewClient = WebViewClient()
            loadUrl(articleUrl)
        }

        // Print the current back stack
//        val navController = findNavController()
//        val backStack = navController.backQueue
//        for (entry in backStack) {
//            Log.d("MyFragment", "Destination ID: ${entry.destination}")
//        }


    }

}