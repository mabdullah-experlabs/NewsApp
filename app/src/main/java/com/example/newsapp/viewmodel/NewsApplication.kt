package com.example.newsapp.viewmodel

import android.app.Application
import com.example.newsapp.koin.appModule
import io.branch.referral.Branch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// add this to manifest also very important
class NewsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the Branch object
        Branch.getAutoInstance(this);

        startKoin{
            androidLogger()
            androidContext(this@NewsApplication)
            modules(appModule)
        }
    }

}