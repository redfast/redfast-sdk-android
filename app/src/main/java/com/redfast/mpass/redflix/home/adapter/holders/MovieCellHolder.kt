package com.redfast.mpass.redflix.home.adapter.holders

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.R
import com.redfast.mpass.api.MovieItem
import com.redfast.mpass.databinding.MovieCellBinding
import com.redfast.mpass.redflix.DetailFragment
import com.squareup.picasso.Picasso

class MovieCellHolder(
    val binding: MovieCellBinding,
    val orientation: String,
    val width: Int,
    val height: Int
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindRow(item: MovieItem) {
        val params = binding.imageView2.layoutParams
        params.width = width
        params.height = height
        binding.imageView2.layoutParams = params

        if (orientation == "portrait") {
            item.portrait?.url?.let {
                Picasso.get()
                    .load(it)
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
        binding.root.setOnClickListener {
            val fragmentManager = (it.context as AppCompatActivity).supportFragmentManager
            val currFragment =
                fragmentManager.findFragmentByTag("Home")
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = DetailFragment(item)
            fragmentTransaction.add(R.id.container, fragment, "Movie")
            fragmentTransaction.hide(currFragment!!)
            fragmentTransaction.addToBackStack("Home")
            fragmentTransaction.commit()
        }
    }
}