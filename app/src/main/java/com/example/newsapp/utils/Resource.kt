package com.example.newsapp.utils

// this is used to wrap around network responses and help us in the loading state
// this is a sealed class like abstract class but we can define which classes can inherit from this class

sealed class Resource<T>(
    val data:T ?=null,
    val message:String ?=null
) {
    class Success<T>(data:T) : Resource<T>(data)
    class Error<T>(message:String,data:T?=null) :Resource<T>(data,message)
    class Loading<T>:Resource<T>()
}