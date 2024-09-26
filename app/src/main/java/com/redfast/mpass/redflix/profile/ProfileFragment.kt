package com.redfast.mpass.redflix.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.redfast.mpass.MainActivity
import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.userId
import com.redfast.mpass.databinding.FragmentProfileBinding
import com.redfast.promotion.PathType
import com.redfast.promotion.PromotionManager

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        PromotionManager.getTriggerablePrompts(screenName = MainActivity.ScreenName.profile.name, type = PathType.VIDEO) {
            val item = it.asList().firstOrNull()
            item?.let { prompt ->
                PromotionManager.showModal(promptId = prompt.id, requireContext()) {
                    Log.d("ProfileFragment", "${it.code}")
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelPlan.setOnClickListener { makeToast("Your plan will be cancelled soon") }
        binding.planHistory.setOnClickListener { makeToast("Your request was sent") }
        binding.changeProfile.setOnClickListener { makeToast("Your profile will be changed soon") }
        binding.settings.setOnClickListener {
            PromotionManager.showDebugView(DefaultSharedPrefs.userId) { newUserId ->
                DefaultSharedPrefs.userId = newUserId
            }
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}
