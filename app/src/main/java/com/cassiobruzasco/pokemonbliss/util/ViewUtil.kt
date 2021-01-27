package com.cassiobruzasco.pokemonbliss.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.cassiobruzasco.pokemonbliss.R

class ViewUtil {
    companion object {
        fun getLoadingDialog(context: Context?): Dialog? {
            if (context == null) return null

            val dialog = Dialog(context, R.style.LoadingDialog)
            if (dialog.window != null) {
                (dialog.window as Window).setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            dialog.setContentView(R.layout.dialog_loading_layout)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

            return dialog
        }
    }
}