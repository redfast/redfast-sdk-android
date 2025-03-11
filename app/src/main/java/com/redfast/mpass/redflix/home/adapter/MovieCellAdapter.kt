package com.redfast.mpass.redflix.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.MovieItemData
import com.redfast.mpass.databinding.MovieCellBinding
import com.redfast.mpass.redflix.home.adapter.holders.MovieCellHolder

class MovieCellAdapter(
    var collections: List<MovieItemData>,
    val orientation: String,
    val width: Int,
    val height: Int
) :
    RecyclerView.Adapter<MovieCellHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCellHolder {
        val binding = MovieCellBinding.inflate(LayoutInflater.from(parent.context))
        return MovieCellHolder(binding, orientation, width, height)
    }

    override fun getItemCount(): Int {
        return collections.count()
    }

    override fun onBindViewHolder(holder: MovieCellHolder, position: Int) {
        collections[position].let {
            holder.bindRow(it.items)
        }
    }
}