package com.darshan.darshanshop.views.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.ActivityLogInBinding
import com.darshan.darshanshop.utils.CommonMethods
import com.darshan.darshanshop.utils.Constants

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLogInBinding
    lateinit var strUsername: String
    lateinit var strPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        binding.btnAddLogin.setOnClickListener {

            if(isValid()){
                savePreferenceValues()
                gotoHomeActivity()
            }

        }


    }

    private fun savePreferenceValues() {
         DarshanShoppingApp.savePreferenceDataBoolean(Constants.IS_LOGGED_IN, true)

        if (CommonMethods.getTrimmedText(binding.edUserName).equals("admin")) {
            DarshanShoppingApp.savePreferenceDataBoolean(Constants.IS_ADMIN, true)
        } else {
            DarshanShoppingApp.savePreferenceDataBoolean(Constants.IS_ADMIN, false)
        }
    }

    private fun gotoHomeActivity() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out)
        finish()
    }

    private fun isValid() : Boolean{

        if(CommonMethods.getTrimmedText(binding.edUserName).isNullOrBlank()){
            CommonMethods.showToast(this@LoginActivity,getString(R.string.msg_enter_user_name))
            return false
        }else if(CommonMethods.getTrimmedText(binding.edPassword).isNullOrBlank()){
            CommonMethods.showToast(this@LoginActivity,getString(R.string.msg_enter_password))
            return false
        }
        return true
    }
}