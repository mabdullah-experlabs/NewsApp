package com.example.newsapp.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.view.NewsActivity
import com.example.newsapp.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.LinkProperties

class ArticleFragment : Fragment(R.layout.fragment_article) {

    // view binding for fragments
    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var branchUniversalObject: BranchUniversalObject



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


        // Initializing Branch.io
        Branch.getAutoInstance(requireContext())




        binding.sharefab.setOnClickListener {
            val article = args.article
            val deepLink = "https://mabdnewsapp.test-app.link/${Uri.encode(article.url)}"
            Log.d("ArticleFragment", "Deep Link is : $deepLink")


            // Create a new BranchUniversalObject to represent the content being shared
            val buo = BranchUniversalObject()
                .setCanonicalIdentifier(deepLink)
                .setTitle(article.title.toString())
                .setContentDescription(article.description)
                .setContentImageUrl(article.urlToImage.toString())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)

            // Generate a short Branch link with link properties
            val lp = LinkProperties()
                .setChannel("Android")
                .setFeature("sharing")
                .addControlParameter("\$fallback_url", deepLink)
            buo.generateShortUrl(requireContext(), lp) { branchLink, error ->
                if (error == null) {
                    // Create a share intent and set the Branch link as the message text
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    //shareIntent.putExtra(Intent.EXTRA_TEXT, branchLink)
                    shareIntent.putExtra(Intent.EXTRA_TEXT, deepLink)


                    // Launch the share intent
                    startActivity(Intent.createChooser(shareIntent, "Share link via"))
                } else {
                    // Handle error
                    Snackbar.make(requireView(), "Error creating Branch link", Snackbar.LENGTH_SHORT).show()
                }
            }
        }


//        //coping the link in user mobile
//        //implicit deep linking
//        binding.sharefab.setOnClickListener{
//            val article = args.article
//            val deepLink = "newsapp.com/viewnews/${Uri.encode(article.url)}"
//            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("News Link", deepLink)
//            clipboard.setPrimaryClip(clip)
//            Snackbar.make(requireView(), "Article Deep Link Copied", Snackbar.LENGTH_SHORT).show()
//        }


    }

    override fun onStart() {
        super.onStart()

        // Register a deep link handler
        Branch.getInstance().initSession({ referringParams, error ->
            if (error == null) {
                // Handle deep link data
                val articleUrl = args.article.url
                if (articleUrl != null) {
                    // Load the article based on the deep link URL
                    val article = args.article
                    article.url?.let { binding.webView.loadUrl(it) }
                }
            } else {
                Log.e("BRANCH", error.message)
            }
        }, requireActivity().intent.data, requireActivity())
    }


}
