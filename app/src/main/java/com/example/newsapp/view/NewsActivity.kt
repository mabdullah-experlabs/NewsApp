package com.example.newsapp.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.viewmodel.NewsViewModel
import io.branch.referral.Branch
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewsActivity : AppCompatActivity() {

    val viewModel by viewModel<NewsViewModel>()

    //view binding for activity
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setting up navigation to different fragments through bottom menu
        binding.bottomNavigationView.setupWithNavController(binding.newsNavHostFragment.getFragment<NavHostFragment>().navController)
    }

    override fun onStart() {
        super.onStart()
        val branch = Branch.getInstance()

        // Branch init
        Branch.sessionBuilder(this).withCallback { referringParams, error ->
            if (error == null) {
                // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                // params will be empty if no data found
                // ... insert custom logic here ...
                Log.i("BRANCH SDK", referringParams.toString())
            } else {
                Log.i("BRANCH SDK", error.message)
            }
        }.withData(this.intent.data).init()
    }

}