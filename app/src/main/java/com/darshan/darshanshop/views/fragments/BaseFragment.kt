package com.darshan.darshanshop.views.fragments

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun showHideProgress(
        isShowProgress: Boolean,
        progressBar: FrameLayout
    ) {
        if (isShowProgress) {
            progressBar.visibility = View.VISIBLE
            progressBar.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            progressBar.isEnabled = true
        }
    }



}