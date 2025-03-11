package com.redfast.mpass.redflix.latest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.api.MovieItemData
import com.redfast.mpass.databinding.ItemLatestBinding
import com.redfast.mpass.redflix.latest.adapter.holders.LatestHolder

class LatestAdapter(
    private val items: List<MovieItemData>
) : RecyclerView.Adapter<LatestHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestHolder {
        val binding = ItemLatestBinding.inflate(LayoutInflater.from(parent.context))
        return LatestHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: LatestHolder, position: Int) {
        holder.bind(items[position].items)
    }
}