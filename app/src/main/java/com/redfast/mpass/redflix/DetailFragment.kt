package com.redfast.mpass.redflix

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.R
import com.redfast.mpass.api.MovieItem
import com.redfast.mpass.api.MovieItemCollection
import com.redfast.mpass.api.Thumbnail
import com.redfast.mpass.databinding.DetailCellBinding
import com.redfast.mpass.databinding.FragmentRedflixDetailBinding
import com.redfast.mpass.redflix.home.adapter.RowAdapter
import com.redfast.promotion.PromotionManager
import com.squareup.picasso.Picasso

class DetailHolder(val binding: DetailCellBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindRow(item: MovieItem) {
        item.portrait?.url?.let {
            Picasso.get()
                .load(it)
                .into(binding.imageView2)
        }
        binding.title.text = item.name
        binding.description.text = item.shortDescription!!
        binding.durationValue.text = item.duration!!
        binding.directorValue.text = item.director!!

        binding.root.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.root.setBackgroundColor(Color.parseColor("#ff0000"))
            } else {
                binding.root.setBackgroundColor(Color.parseColor("#00000000"))
            }
        }
    }
}

class DetailCellAdapter(var collections: List<MovieItem>, val height: Int) :
    RecyclerView.Adapter<DetailHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
        val binding = DetailCellBinding.inflate(LayoutInflater.from(parent.context))
        val metrics = Resources.getSystem().displayMetrics
        binding.root.layoutParams = ViewGroup.LayoutParams(metrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
        return DetailHolder(binding)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: DetailHolder, position: Int) {
        collections[position].let {
            holder.bindRow(it)
        }
    }
}

class DetailFragment(val item: MovieItem) : Fragment() {
    private fun makeBanner(url: String, height: Int) =
        MovieItemCollection(
            "banner", "", 0, height, listOf(
                MovieItem(
                    "",
                    "",
                    "",
                    "",
                    Thumbnail(url),
                    null,
                    true
                )
            )
        )

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentRedflixDetailBinding.inflate(inflater, container, false).apply {
                val rowAdapter = RowAdapter(listOf())
                content.layoutManager = LinearLayoutManager(this@DetailFragment.context)
                content.adapter = rowAdapter

                val movieRows = mutableListOf<MovieItemCollection>()
                movieRows.add(
                    MovieItemCollection(
                        "detail", "", 0, 640, listOf(
                            item
                        )
                    )
                )
                movieRows.add(
                    this@DetailFragment.makeBanner(
                        R.drawable.description1.toString(),
                        270
                    )
                )
                movieRows.add(
                    this@DetailFragment.makeBanner(
                        R.drawable.description2.toString(),
                        340
                    )
                )
                movieRows.add(
                    this@DetailFragment.makeBanner(
                        R.drawable.description3.toString(),
                        640
                    )
                )
                movieRows.add(
                    this@DetailFragment.makeBanner(
                        R.drawable.description4.toString(),
                        450
                    )
                )
                movieRows.add(
                    this@DetailFragment.makeBanner(
                        R.drawable.description5.toString(),
                        640
                    )
                )
                rowAdapter.collections = movieRows
                rowAdapter.notifyDataSetChanged()
            }
        PromotionManager.setScreenName(binding.root, "Movie Detail") {}
        binding.content.requestFocus()
        return binding.root
    }
}