package com.example.newsapp.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

//custom function to set image with glide through data binding
@BindingAdapter("imageFromUrl")
fun ImageView.imageFromUrl(url:String?){
    if (url != null){
    Glide.with(context).load(url).into(this)
    }
}