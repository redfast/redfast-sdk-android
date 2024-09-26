package com.redfast.mpass

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.redfast.mpass.databinding.AccountModalBinding
import com.redfast.promotion.PromotionManager


class AccountModal(content: Context) : Dialog(content) {
    private lateinit var binding: AccountModalBinding

    fun showDialog() {
        binding = AccountModalBinding.inflate(LayoutInflater.from(context))
        binding.dialog = this
        setContentView(binding.root)
        var width = Resources.getSystem().displayMetrics.widthPixels
        var height = Resources.getSystem().displayMetrics.heightPixels
        width = when (Utils.getDeviceType()) {
            Utils.DeviceType.tablet -> width - 200
            else -> width - 10
        }
        height = when (Utils.getDeviceType()) {
            Utils.DeviceType.tablet -> height - 250
            else -> height - 10
        }
        var params = binding.modalImage.layoutParams
        params.height = when (Utils.getDeviceType()) {
            Utils.DeviceType.tablet -> height - 180
            else -> height - 350
        }
        params.width = params.height * 3 / 5
        binding.modalImage.layoutParams = params
        show()

        window?.attributes?.let{ lp ->
            lp.gravity = Gravity.TOP
            window?.attributes = lp
        }
        window?.setLayout(width, height)
    }

    fun reset(view: View) {
        PromotionManager.resetGoal()
        dismiss()
    }

    fun switchAcount(view: View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Change user Id")
        val view = LayoutInflater.from(context).inflate(R.layout.account_new_user, null)
        val userId = view.findViewById(R.id.username) as EditText
        builder.setView(view)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    PromotionManager.setUserId(userId.text.toString())
                    dialog.dismiss()
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                })
        builder.create().show()
    }
}