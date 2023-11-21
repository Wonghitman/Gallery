package com.example.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive : LiveData<List<PhotoItem>>
        get() = _photoListLive

    fun fetchData() {
        val StringRequest = StringRequest(
                Request.Method.GET,
                getUrl(),
                {
                    _photoListLive.value = Gson().fromJson(it,Pixabay::class.java).hits.toList()

                },
                {
                    Log.d("hello",it.toString())
                }
        )
        VollySingleton.getInstance(getApplication()).requestQueue.add(StringRequest)
    }

    private fun getUrl():String{
        return "https://pixabay.com/api/?key=40746625-6dd3135dd043554f3b934d632&q=${keyWords.random()}"
    }
    private val keyWords = arrayOf("cat","dog","car","beauty","phone","computer","flower","animal")
}