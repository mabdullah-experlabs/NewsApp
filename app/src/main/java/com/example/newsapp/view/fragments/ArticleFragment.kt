package com.example.newsapp.view.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.view.NewsActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    // view binding for fragments
    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: NewsViewModel


    private val args : ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentArticleBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel



        binding.webView.apply {
            webViewClient = WebViewClient()
            val article = args.article
            article.url?.let { loadUrl(it) }
        }

        //saving the article in db
        binding.fab.setOnClickListener{
            val article = args.article
            viewModel.saveArticle(article)
            val snackbar = Snackbar.make(
                requireView(),
                "Article Saved Successfully",
                Snackbar.LENGTH_SHORT
            )
            snackbar.show()
        }

        //newsapp://www.mabdnewsapp.com/viewnews/{articleUrl}


        //coping the link in user mobile
        //implicit deep linking
        binding.sharefab.setOnClickListener{
            val article = args.article
            val deepLink = "newsapp.com/viewnews/${Uri.encode(article.url)}"
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("News Link", deepLink)
            clipboard.setPrimaryClip(clip)
            Snackbar.make(requireView(), "Article Deep Link Copied", Snackbar.LENGTH_SHORT).show()
        }

    }

}
