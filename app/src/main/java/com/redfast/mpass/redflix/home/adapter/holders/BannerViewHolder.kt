package com.redfast.mpass.redflix.home.adapter.holders

import android.app.AlertDialog
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redfast.mpass.MovieFragment
import com.redfast.mpass.api.MovieItem
import com.redfast.promotion.IapProductType
import com.redfast.promotion.PromotionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.graphics.toColorInt
import com.redfast.mpass.R
import com.redfast.mpass.databinding.MovieCellBinding

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
                Glide.with(binding.imageView2)
                    .load(item.landscape.url.toInt())
                    .into(binding.imageView2)
            }
        } else {
            item.landscape?.url?.let {
                Glide.with(binding.imageView2)
                    .load(it)
                    .into(binding.imageView2)
            }
        }
        binding.root.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.root.setBackgroundColor("#ff0000".toColorInt())
            } else {
                binding.root.setBackgroundColor("#00000000".toColorInt())
            }
        }
        binding.root.setOnClickListener {
            val owner: LifecycleOwner = binding.root.findViewTreeLifecycleOwner()
                ?: (binding.root.context as? AppCompatActivity)
                ?: return@setOnClickListener

            PromotionManager.iapGetProductDetails(
                "recurly_media_pack",
                IapProductType.consumable
            ) { list ->
                owner.lifecycleScope.launch(Dispatchers.Main) {
                    if (list.isNotEmpty()) {
                        val builder = AlertDialog.Builder(binding.root.context)
                        builder.setTitle("InApp Purchase")
                        builder.setMessage("Do you want to purchase " + list[0].title + " for " + list[0].price)
                        builder.setPositiveButton("YES") { _, _ ->
                            PromotionManager.iapPurchaseProducts(
                                listOf(list[0].rawObj),
                                null
                            ) { purchases, error ->
                                val fragmentManager =
                                    (it.context as AppCompatActivity).supportFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                val fragment = MovieFragment()
                                fragmentTransaction.add(R.id.container, fragment, "Movie")
                                fragmentTransaction.addToBackStack("Home")
                                fragmentTransaction.commit()
                            }
                        }
                        builder.create().show()
                    }
                }
            }
        }
    }
}