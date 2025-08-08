package com.redfast.mpass

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.redfast.mpass.base.BaseActivity
import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.userId
import com.redfast.mpass.redflix.genres.GenresFragment
import com.redfast.mpass.redflix.home.HomeFragment
import com.redfast.mpass.redflix.latest.LatestFragment
import com.redfast.mpass.redflix.profile.ProfileFragment
import com.redfast.promotion.PROMPT_ID_KEY
import com.redfast.promotion.PromotionManager
import com.redfast.promotion.SCREEN_NAME_KEY
import com.redfast.promotion.SKU_ID_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val APP_ID = "YOUR_APP_ID"

class MainActivity : BaseActivity() {

    enum class ScreenName {
        home, latest, genres, profile, unknown
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val logoParent = findViewById<LinearLayout>(R.id.logoParent)
        logoParent.addTopMarginIfSdk35()
        PromotionManager.initPromotion(this, APP_ID, DefaultSharedPrefs.userId) {
            GlobalScope.launch(Dispatchers.Main) {
                setUpBottomNavigation()
                handleDeeplink(intent)
            }
        }
    }

    fun View.addTopMarginIfSdk35() {
        if (Build.VERSION.SDK_INT >= 35) {
            val layoutParams = this.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.let {
                val marginInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    32f,
                    this.resources.displayMetrics
                ).toInt()
                it.topMargin += marginInPx
                this.layoutParams = it
            }
        }
    }

    private fun setUpBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    switchFragment(ScreenName.home)
                    true
                }

                R.id.latest -> {
                    switchFragment(ScreenName.latest)
                    true
                }

                R.id.genres -> {
                    switchFragment(ScreenName.genres)
                    true
                }

                R.id.profile -> {
                    switchFragment(ScreenName.profile)
                    true
                }

                else -> false
            }
        }
        bottomNavigation.setSelectedItemId(R.id.home)
    }

    private fun switchFragment(screenName: ScreenName) {
        val fragmentManager = supportFragmentManager
        val pair: Pair<Fragment, String>? = when (screenName) {
            ScreenName.home -> HomeFragment() to "Home"
            ScreenName.latest -> LatestFragment() to "Latest"
            ScreenName.genres -> GenresFragment() to "Genres"
            ScreenName.profile -> ProfileFragment() to "Profile"
            else -> null
        }
        pair?.let {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, pair.first, pair.second)
            fragmentTransaction.commit()
        }
    }

    private fun handleDeeplink(intent: Intent?) {
        intent?.getStringExtra(PROMPT_ID_KEY)?.let {
            PromotionManager.showModal(it, this) {
                Toast.makeText(this, "Completed", Toast.LENGTH_LONG).show()
            }
            return
        }
        intent?.getStringExtra(SKU_ID_KEY)?.let {
            PromotionManager.purchaseIap(it) {
                Toast.makeText(this, "Completed", Toast.LENGTH_LONG).show()
            }
            return
        }
        val deepLinkScreen = intent?.getStringExtra(SCREEN_NAME_KEY)?.trim()?.lowercase()
        if (!deepLinkScreen.isNullOrEmpty()) {
            showScreen(deepLinkScreen)
        }
    }

    private fun showScreen(deeplinkToScreen: String?) {
        val screen = deeplinkToScreen?.let {
            try {
                ScreenName.valueOf(it)
            } catch (e: IllegalArgumentException) {
                ScreenName.home
            }
        } ?: ScreenName.home
        findViewById<BottomNavigationView?>(R.id.bottom_navigation)
            .selectedItemId = screenNameToItemId(screen)
        switchFragment(screen)
    }

    private fun screenNameToItemId(screenName: ScreenName): Int {
        return when (screenName) {
            ScreenName.profile -> R.id.profile
            ScreenName.latest -> R.id.latest
            ScreenName.genres -> R.id.genres
            else -> R.id.home
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplink(intent)
    }
}
