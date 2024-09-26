package com.redfast.mpass.redflix.genres.adapter.holders

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.MovieFragment
import com.redfast.mpass.R
import com.redfast.promotion.PromotionManager
import com.squareup.picasso.Picasso

class BillboardHolder(v: View) : RecyclerView.ViewHolder(v) {
    init {
        itemView.setOnClickListener {
            PromotionManager.getIapItems("com.redfast.test.consumable") { list ->
                if (list.isNotEmpty()) {
                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setTitle("InApp Purchase")
                    builder.setMessage("Do you want to purchase " + list[0].title + " for " + list[0].price)
                    builder.setPositiveButton("YES") { _, _ ->
                        PromotionManager.purchaseIap(list[0].sku!!) {
                            val fragmentManager =
                                (v.context as AppCompatActivity).supportFragmentManager
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

    fun bindBillboard(url: String) {
        Picasso.get()
            .load(url)
            .into(itemView as ImageView)
    }
}