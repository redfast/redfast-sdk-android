package com.redfast.mpass.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.redfast.mpass.api.MovieItemCollection
import com.redfast.mpass.api.RedflixApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RedflixModels {
    private val redflixApi = RedflixApi()

    fun fetchCollectionItems(colledtionId: String): LiveData<MovieItemCollection> {
        val data = MutableLiveData<MovieItemCollection>()
        GlobalScope.launch {
            redflixApi.getSiteCollectionItems(colledtionId)?.let {
                data.postValue(it)
            }
        }
        return data
    }
}