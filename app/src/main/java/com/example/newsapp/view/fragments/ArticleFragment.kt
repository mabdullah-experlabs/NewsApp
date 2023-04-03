package com.example.newsapp.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.model.data_classes.Article
import com.example.newsapp.view.NewsActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    // view binding for fragments
    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: NewsViewModel

    //sending data back to first fragment with Interface callback
    //private var articleInterface: ArticleInterface? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel


//        val navController = Navigation.findNavController(view)
//
//        val previousBackStackEntry = navController.previousBackStackEntry
//        if (previousBackStackEntry != null) {
//            Log.i("Masla-BackStack", parentFragmentManager.findFragmentById(previousBackStackEntry.destination.id).toString())
//
//            articleInterface = parentFragmentManager.findFragmentById(previousBackStackEntry.destination.id) as? ArticleInterface
//        }

        //val args:ArticleFragmentArgs by navArgs()
        //val url=args.articleUrl

        val article = if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("article",Article::class.java)
        }else{
            arguments?.getSerializable("article") as? Article
        }

        binding.webView.apply {
            webViewClient = WebViewClient()
            if (article != null) {
                loadUrl(article.url)
            }
        }

        //saving the article in db
        binding.fab.setOnClickListener{
            if (article != null) {
                viewModel.saveArticle(article)
                val snackbar = Snackbar.make(requireView(), "Article Saved Successfully", Snackbar.LENGTH_SHORT)
                snackbar.show()
//                Log.i("Masla-Article", articleInterface.toString())
//                articleInterface?.onTitleReceived(article.title)
            }
        }

    }

}

//interface ArticleInterface{
//    fun onTitleReceived(title:String)
//}