package com.redfast.mpass.redflix.genres.adapter.holders

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.Genre
import com.redfast.mpass.databinding.MovieRowBinding
import com.redfast.mpass.redflix.genres.GenreViewModel
import com.redfast.mpass.redflix.genres.adapter.CellAdapter

class MovieRowHolder(
    private val viewModel: GenreViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    val binding: MovieRowBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bindGenre(genre: Genre, position: Int) {
        binding.textView4.text = genre.name
        binding.movieCells.layoutManager =
            LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        val movieCellAdapter = CellAdapter(listOf(), position)
        binding.movieCells.adapter = movieCellAdapter
        viewModel.loadGenreTVs(genre.id).observe(viewLifecycleOwner) {
            movieCellAdapter.list = it
            movieCellAdapter.notifyDataSetChanged()
        }
    }
}