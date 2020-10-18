package com.darshan.darshanshop.views.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.ActivityHomeBinding
import com.darshan.darshanshop.views.fragments.ShoppingListFragment


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DarshanShoppingApp.activityFromApp = this
        init()
        openFragment(ShoppingListFragment())
    }

    private fun init() {
        setBinding()
    }
    private fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
    }

    fun openFragment(mFragment: Fragment?) {
        val transaction =
            supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, mFragment!!)
        transaction.commit()
    }
}