package com.redfast.mpass.redflix.genres.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.Genre
import com.redfast.mpass.databinding.MovieRowBinding
import com.redfast.mpass.redflix.genres.GenreViewModel
import com.redfast.mpass.redflix.genres.adapter.holders.MovieRowHolder

class RowAdapter(
    private val viewModel: GenreViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    var collections: Map<Int, Genre>
) : RecyclerView.Adapter<MovieRowHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieRowHolder {
        val binding = MovieRowBinding.inflate(LayoutInflater.from(parent.context))
        return MovieRowHolder(viewModel, viewLifecycleOwner, binding)
    }

    override fun getItemCount(): Int {
        return collections.count()
    }

    override fun onBindViewHolder(holder: MovieRowHolder, position: Int) {
        val key = collections.keys.toTypedArray()[position]
        collections[key]?.let {
            holder.bindGenre(it, position)
        }
    }
}