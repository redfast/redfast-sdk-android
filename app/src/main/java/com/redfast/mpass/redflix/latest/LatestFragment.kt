package com.redfast.mpass.redflix.latest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.redfast.mpass.MainActivity
import com.redfast.mpass.Utils
import com.redfast.mpass.databinding.FragmentLatestBinding
import com.redfast.mpass.redflix.home.HomeViewModel
import com.redfast.mpass.redflix.latest.adapter.LatestAdapter
import com.redfast.promotion.PathType
import com.redfast.promotion.PromotionManager
import java.util.*

class LatestFragment : Fragment() {

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentLatestBinding.inflate(inflater, container, false).apply {
                val viewModel = HomeViewModel()

                viewModel.loadCollections().observe(viewLifecycleOwner) {
                    val adapter = LatestAdapter(it.items)
                    content.layoutManager =
                        GridLayoutManager(this@LatestFragment.context, getNumberOfColumns())
                    content.adapter = adapter
                }
            }

        PromotionManager.getTriggerablePrompts(screenName = MainActivity.ScreenName.latest.name, type = PathType.MODAL) {
            val item = it.asList().firstOrNull()
            item?.let { prompt ->
                PromotionManager.showModal(promptId = prompt.id, requireContext()) {
                    Log.d("LatestFragment", "${it.code}")
                }
            }
        }
        return binding.root
    }

    private fun getNumberOfColumns(): Int {
        val tablet = Utils.isTablet()
        val landscape = Utils.isLandscape()
        return when {
            tablet && landscape -> 4
            tablet || landscape -> 3
            else -> 2
        }
    }
}