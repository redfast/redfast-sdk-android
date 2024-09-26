package com.redfast.mpass.redflix.home.adapter.holders

import android.content.res.Resources
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.MovieItem
import com.redfast.mpass.databinding.MovieCellBinding
import com.squareup.picasso.Picasso

class BannerViewHolder(val binding: MovieCellBinding, val height: Int) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindRow(item: MovieItem) {
        val metrics = Resources.getSystem().displayMetrics
        val params = binding.imageView2.layoutParams
        params.width = metrics.widthPixels
        params.height = height
        binding.imageView2.layoutParams = params

        if (item.local!!) {
            item.landscape?.url?.let {
                Picasso.get()
                    .load(item.landscape.url.toInt())
                    .into(binding.imageView2)
            }
        } else {
            item.landscape?.url?.let {
                Picasso.get()
                    .load(it)
                    .into(binding.imageView2)
            }
        }
        binding.root.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.root.setBackgroundColor(Color.parseColor("#ff0000"))
            } else {
                binding.root.setBackgroundColor(Color.parseColor("#00000000"))
            }
        }
    }
}