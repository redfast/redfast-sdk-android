package com.redfast.mpass.redflix.genres.adapter

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.redfast.mpass.redflix.genres.adapter.holders.BillboardHolder
import com.redfast.promotion.PathItem

class BillboardsAdapter(private val context: Context, private val items: Array<PathItem>) :
    RecyclerView.Adapter<BillboardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillboardHolder {
        val view = ImageView(parent.context)
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.isFocusable = true
        return BillboardHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BillboardHolder, position: Int) {
        val uiModeManager =
            context.getSystemService(AppCompatActivity.UI_MODE_SERVICE) as UiModeManager
        var backgroundUrl =
            if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
                items[position].actions?.rf_settings_bg_image_android_os_fire_tv_composite
            } else if (isTablet()) {
                items[position].actions?.rf_settings_bg_image_android_os_tablet_composite
            } else {
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    items[position].actions?.rf_settings_bg_image_android_os_phone_composite
                else
                    items[position].actions?.rf_settings_bg_image_android_os_tablet_composite
            }
        backgroundUrl?.let {
            holder.bindBillboard(it)
        }
    }

    fun isTablet(): Boolean {
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val wi = width.toDouble() / displayMetrics.xdpi.toDouble()
        val hi = height.toDouble() / displayMetrics.ydpi.toDouble()
        val x = Math.pow(wi, 2.0)
        val y = Math.pow(hi, 2.0)
        val screenInches = Math.sqrt(x + y)
        return screenInches > 7
    }
}