package com.redfast.mpass.redflix.genres.adapter.holders

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redfast.mpass.MovieFragment
import com.redfast.mpass.R
import com.redfast.promotion.IapProductType
import com.redfast.promotion.PromotionManager

class BillboardHolder(v: View) : RecyclerView.ViewHolder(v) {
    init {
        itemView.setOnClickListener {
            PromotionManager.iapGetProductDetails("com.redfast.test.consumable", IapProductType.consumable) { products ->
                if (products.isNotEmpty()) {
                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setTitle("InApp Purchase")
                    builder.setMessage("Do you want to purchase " + products[0].title + " for " + products[0].price)
                    builder.setPositiveButton("YES") { _, _ ->
                        PromotionManager.iapPurchaseProducts(listOf(products[0].rawObj), null) { purchases, error ->
                            if (error == null && purchases.isNotEmpty()) {
                                val fragmentManager = (v.context as AppCompatActivity).supportFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                val fragment = MovieFragment()
                                fragmentTransaction.add(R.id.container, fragment, "Movie")
                                fragmentTransaction.addToBackStack("Home")
                                fragmentTransaction.commit()
                            }
                        }
                    }
                    builder.create().show()
                }
            }
        }
    }

    fun bindBillboard(url: String) {
        val view = itemView as ImageView
        Glide.with(view.context)
            .load(url)
            .into(view)
    }
}