package com.redfast.mpass.redflix.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.MovieItem
import com.redfast.mpass.databinding.MovieCellBinding
import com.redfast.mpass.redflix.home.adapter.holders.BannerViewHolder

class BannerCellAdapter(var collections: List<MovieItem>, val height: Int) :
    RecyclerView.Adapter<BannerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = MovieCellBinding.inflate(LayoutInflater.from(parent.context))
        return BannerViewHolder(binding, height)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        collections[position].let {
            holder.bindRow(it)
        }
    }
}