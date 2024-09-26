package com.redfast.mpass.redflix.latest.adapter.holders

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.R
import com.redfast.mpass.Utils
import com.redfast.mpass.api.MovieItem
import com.redfast.mpass.databinding.ItemLatestBinding
import com.redfast.mpass.redflix.DetailFragment
import com.squareup.picasso.Picasso

class LatestHolder(val binding: ItemLatestBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MovieItem) {
        val image = if (Utils.isLandscape()) item.landscape?.url else item.portrait?.url
        Picasso.get()
            .load(image)
            .into(binding.image)

        binding.root.setOnClickListener {
            val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
            val currFragment =
                fragmentManager.findFragmentByTag("Latest")
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = DetailFragment(item)
            fragmentTransaction.add(R.id.container, fragment, "Movie")
            fragmentTransaction.hide(currFragment!!)
            fragmentTransaction.addToBackStack("Latest")
            fragmentTransaction.commit()
        }
    }
}