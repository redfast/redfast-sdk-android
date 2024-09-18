package com.redfast.mpass.redflix.genres

import androidx.databinding.BaseObservable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.redfast.mpass.api.Genre
import com.redfast.mpass.api.Video
import com.redfast.mpass.model.TmdbModels

class GenreViewModel(private val lifecycleOwner: LifecycleOwner) : BaseObservable() {
    private val model = TmdbModels()

    fun loadGenres(): LiveData<Map<Int, Genre>> {
        val collections = MutableLiveData<Map<Int, Genre>>()
        model.fetchTvGenres().observe(lifecycleOwner) { genres ->
            val allGenres = mutableMapOf<Int, Genre>()
            genres.genres.forEach {
                val genre = Genre(it.id, it.name)
                allGenres[it.id] = genre
            }
            collections.value = allGenres
        }
        return collections
    }

    fun loadGenreTVs(genreId: Int): LiveData<List<Video>> {
        val collections = MutableLiveData<List<Video>>()
        model.fetchTvByGenre(genreId).observe(lifecycleOwner) {
            collections.value = it.results
        }
        return collections
    }
}