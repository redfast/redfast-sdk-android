package com.redfast.mpass.redflix.genres.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.Video
import com.redfast.mpass.databinding.MovieCellBinding
import com.redfast.mpass.redflix.genres.adapter.holders.MovieCellHolder

class CellAdapter(var list: List<Video>, var row: Int) :
    RecyclerView.Adapter<MovieCellHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCellHolder {
        val binding = MovieCellBinding.inflate(LayoutInflater.from(parent.context))
        binding.root.isFocusable = true
        return MovieCellHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: MovieCellHolder, cell: Int) {
        holder.bindCell(list[cell], row, cell)
    }
}