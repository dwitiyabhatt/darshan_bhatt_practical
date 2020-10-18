package com.darshan.darshanshop.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.ActivitySplashBinding
import com.darshan.darshanshop.utils.CommonMethods
import com.darshan.darshanshop.utils.Constants

class SplashScreenActivity : AppCompatActivity() {
     private lateinit var binding:ActivitySplashBinding
     private lateinit var uptodown: Animation
     private lateinit var downtoup: Animation
     private lateinit var rotateAnimation: RotateAnimation



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAnimations()
        //startAnimmations()
    }

    private fun initializeAnimations() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        uptodown = AnimationUtils.loadAnimation(this, R.anim.splash_up_down)
        downtoup = AnimationUtils.loadAnimation(this, R.anim.splash_down_up)
        rotateAnimation = RotateAnimation(
            0.0f,
            720.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 1500
        rotateAnimation.interpolator = LinearInterpolator()
    }

    private fun startAnimmations() {
        binding.titleimage.animation = downtoup
        binding.titletxt.animation = uptodown
        binding.ivLogo.startAnimation(rotateAnimation)
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            redirectToNextScreen()

            // close this activity
            finish()
        }, 2500)
    }

    override fun onStart() {
        super.onStart()
        startAnimmations()
    }


    private fun redirectToNextScreen() {
        if (DarshanShoppingApp.sharedPreferences.getBoolean(
                Constants.IS_LOGGED_IN,
                false
            )
        ) {
            CommonMethods.changeActivity(this@SplashScreenActivity, HomeActivity::class.java,true)
        } else {
            CommonMethods.changeActivity(this@SplashScreenActivity, LoginActivity::class.java,true)

        }

    }
}