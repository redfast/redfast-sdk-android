package com.redfast.mpass.redflix.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.MovieItemCollection
import com.redfast.mpass.databinding.RedflixRowBinding
import com.redfast.mpass.redflix.home.adapter.holders.RowHolder

class RowAdapter(var collections: List<MovieItemCollection>) :
    RecyclerView.Adapter<RowHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val binding = RedflixRowBinding.inflate(LayoutInflater.from(parent.context))
        return RowHolder(binding)
    }

    override fun getItemCount(): Int {
        return collections.count()
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        collections[position].let {
            holder.bindRow(it)
        }
    }
}