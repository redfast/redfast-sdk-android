package com.redfast.mpass.redflix.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.redfast.mpass.R
import com.redfast.mpass.api.MovieItem
import com.redfast.mpass.api.MovieItemCollection
import com.redfast.mpass.api.MovieItemData
import com.redfast.mpass.api.Thumbnail
import com.redfast.mpass.databinding.FragmentHomeBinding
import com.redfast.mpass.redflix.home.adapter.RowAdapter
import com.redfast.promotion.InlineType
import com.redfast.promotion.PromotionManager
import com.redfast.promotion.logic.BgImageProvider

class HomeFragment : Fragment() {
    private fun makeBanner(url: String, height: Int, local: Boolean) =
        MovieItemCollection(
            "banner", "", 0, height, listOf(
                MovieItemData(
                    MovieItem(
                        "",
                        "",
                        "",
                        "",
                        Thumbnail(url),
                        null,
                        local
                    )
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
            FragmentHomeBinding.inflate(inflater, container, false).apply {
                val viewModel = HomeViewModel()
                val rowAdapter = RowAdapter(listOf())
                content.layoutManager = LinearLayoutManager(this@HomeFragment.context)
                content.adapter = rowAdapter

                PromotionManager.getInlines(InlineType.general) { inlines ->
                    viewModel.loadCollections().observe(viewLifecycleOwner, Observer {
                        val movieRows = mutableListOf<MovieItemCollection>()

                        val portraits = it.items.slice(IntRange(0, it.items.count() / 2))
                        val landscapes = it.items.slice(IntRange(it.items.count() / 2, it.items.count() - 1))
                        if (inlines.isNotEmpty()) {
                            val inlineActions = inlines[0].actions
                            val bgImageUrl = BgImageProvider(inlineActions).getBgImage() ?: inlineActions?.rf_settings_bg_image_android_os_fire_tv_composite
                            movieRows.add(
                                this@HomeFragment.makeBanner(
                                    bgImageUrl ?: "",
                                    440,
                                    false
                                )
                            )
                        }
                        movieRows.add(
                            this@HomeFragment.makeBanner(
                                R.drawable.highlightd.toString(),
                                270,
                                true
                            )
                        )
                        movieRows.add(
                            MovieItemCollection(
                                "movie",
                                "portrait",
                                270, 390, portraits
                            )
                        )
                        movieRows.add(
                            this@HomeFragment.makeBanner(
                                R.drawable.splash.toString(),
                                500,
                                true
                            )
                        )
                        movieRows.add(
                            this@HomeFragment.makeBanner(
                                R.drawable.new_release.toString(),
                                250,
                                true
                            )
                        )
                        movieRows.add(
                            MovieItemCollection(
                                "movie",
                                "landscape",
                                560, 320, landscapes
                            )
                        )
                        rowAdapter.collections = movieRows
                        rowAdapter.notifyDataSetChanged()
                    })
                }
            }
        PromotionManager.setScreenName (binding.root, "home") {  }
        binding.content.requestFocus()
        return binding.root
    }
}