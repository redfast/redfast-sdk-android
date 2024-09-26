package com.redfast.mpass.redflix.genres.adapter.holders

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.MovieFragment
import com.redfast.mpass.R
import com.redfast.mpass.api.Video
import com.redfast.mpass.databinding.MovieCellBinding
import com.squareup.picasso.Picasso

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
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w200" + video.poster_path)
            .into(binding.imageView2)
    }
}