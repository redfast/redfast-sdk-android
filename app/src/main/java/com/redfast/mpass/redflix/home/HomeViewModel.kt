package com.redfast.mpass.redflix.home

import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import com.redfast.mpass.api.MovieItemCollection
import com.redfast.mpass.model.RedflixModels

class HomeViewModel: BaseObservable() {
    private val model = RedflixModels()

    fun loadCollections(): LiveData<MovieItemCollection> =
        model.fetchCollectionItems("635c3e79a327a596b2d7a7cd")
}