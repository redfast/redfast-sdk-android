package com.redfast.mpass.redflix.genres

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.MainActivity
import com.redfast.mpass.api.Genre
import com.redfast.mpass.databinding.FragmentGenresBinding
import com.redfast.mpass.redflix.genres.adapter.BillboardsAdapter
import com.redfast.mpass.redflix.genres.adapter.RowAdapter
import com.redfast.promotion.InlineType
import com.redfast.promotion.PathType
import com.redfast.promotion.PromotionManager
import com.redfast.promotion.PromotionResultCode

class GenresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentGenresBinding.inflate(inflater, container, false).apply {
                val viewModel = GenreViewModel(viewLifecycleOwner)
                val movieRowAdapter = RowAdapter(viewModel, viewLifecycleOwner, mapOf())
                recyclerView.layoutManager = LinearLayoutManager(this@GenresFragment.context)
                recyclerView.adapter = movieRowAdapter

                viewModel.loadGenres().observe(viewLifecycleOwner) {
                    val allGenres = mutableMapOf<Int, Genre>()
                    allGenres.putAll(it)
                    movieRowAdapter.collections = allGenres
                    movieRowAdapter.notifyDataSetChanged()
                    billboards.layoutManager =
                        LinearLayoutManager(
                            this@GenresFragment.context,
                            RecyclerView.HORIZONTAL,
                            false
                        )
                    PromotionManager.getInlines(InlineType.general) {
                        billboards.adapter =
                            BillboardsAdapter(this@GenresFragment.requireContext(), it)
                        billboards.isVisible = it.isNotEmpty()
                    }
                }
            }
        binding.unsubscribe.setOnFocusChangeListener { view: View, hasFocus: Boolean ->
            if (hasFocus)
                view.setBackgroundColor(Color.WHITE)
            else
                view.setBackgroundColor(Color.TRANSPARENT)
        }
        binding.unsubscribe.requestFocus()
        binding.unsubscribe.setOnClickListener { onClick(it) }
        PromotionManager.getTriggerablePrompts(screenName = MainActivity.ScreenName.genres.name, type = PathType.BOTTOM_BANNER) {
            val item = it.asList().firstOrNull()
            item?.let { prompt ->
                PromotionManager.showModal(promptId = prompt.id, requireContext()) {
                    Log.d("HomeFragment", "${it.code}")
                }
            }
        }
        return binding.root
    }

    fun onClick(view: View) {
        context?.let {
            PromotionManager.buttonClick(view, "accessibility-123") {
                when (it.code) {
                    PromotionResultCode.timeout,
                    PromotionResultCode.button3,
                    PromotionResultCode.dismiss ->
                        Log.v("mpass", "offer not selected")

                    PromotionResultCode.button1 ->
                        Log.v("mpass", "offer accepted")

                    else ->
                        Log.v("mpass", "not applicable")
                }
            }
        }
    }
}