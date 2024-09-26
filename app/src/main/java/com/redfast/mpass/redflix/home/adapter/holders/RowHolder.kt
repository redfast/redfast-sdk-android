package com.redfast.mpass.redflix.home.adapter.holders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.MovieItemCollection
import com.redfast.mpass.databinding.RedflixRowBinding
import com.redfast.mpass.redflix.DetailCellAdapter
import com.redfast.mpass.redflix.home.adapter.BannerCellAdapter
import com.redfast.mpass.redflix.home.adapter.MovieCellAdapter
import java.util.Locale

class RowHolder(val binding: RedflixRowBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindRow(siteItems: MovieItemCollection) {
        LinearLayoutManager(
            binding.root.context,
            RecyclerView.HORIZONTAL,
            false
        ).also { binding.rowCells.layoutManager = it }
        val params = binding.rowCells.layoutParams
        binding.rowCells.adapter = when (siteItems.name?.uppercase(Locale.ROOT)) {
            "BANNER" -> {
                params.height = siteItems.height ?: 300
                binding.rowCells.layoutParams = params
                BannerCellAdapter(siteItems.items, params.height)
            }

            "MOVIE" -> {
                params.height = siteItems.height ?: 350
                binding.rowCells.layoutParams = params
                MovieCellAdapter(
                    siteItems.items,
                    siteItems.orientation!!,
                    siteItems.width!!,
                    params.height
                )
            }

            "DETAIL" -> {
                params.height = siteItems.height ?: 350
                binding.rowCells.layoutParams = params
                DetailCellAdapter(
                    siteItems.items,
                    params.height
                )
            }

            else -> null
        }
    }
}