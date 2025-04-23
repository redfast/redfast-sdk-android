package com.redfast.mpass.redflix.genres.adapter.holders

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redfast.mpass.MovieFragment
import com.redfast.mpass.R
import com.redfast.mpass.api.Video
import com.redfast.mpass.databinding.MovieCellBinding

class MovieCellHolder(val binding: MovieCellBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = MovieFragment()
            fragmentTransaction.add(R.id.container, fragment, "Movie")
            fragmentTransaction.addToBackStack("Home")
            fragmentTransaction.commit()
        }
    }

    fun bindCell(video: Video, row: Int, cell: Int) {
        Glide.with(binding.imageView2)
            .load("https://image.tmdb.org/t/p/w200" + video.poster_path)
            .into(binding.imageView2)
    }
}