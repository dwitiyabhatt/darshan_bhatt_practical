package com.darshan.darshanshop.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.darshan.darshanshop.R
import com.darshan.darshanshop.views.activities.SplashScreenActivity
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


object CommonMethods {

    const val SHOULD_PRINT_LOG : Boolean = true



    fun showLog(tag:String ,message : String){
        if(SHOULD_PRINT_LOG) Log.d(tag,message)
    }

    fun changeActivity(
        currentActivity: Activity,
        cls: Class<*>?,
        shouldFinish: Boolean
    ) {
        val mIntent = Intent(currentActivity, cls)
        currentActivity.startActivity(mIntent)
        currentActivity.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out)
        if (shouldFinish) currentActivity.finish()
    }

    fun hideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getTrimmedText(editText: EditText): String? {
        return editText.text.toString().trim { it <= ' ' }
    }

    fun showToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("WrongConstant")
    fun showSnackBar(rootView : View, message:String){
        val snack = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
        snack.show()
    }

    fun addNextFragment(
        mActivity: AppCompatActivity, targetedFragment: Fragment,
        shooterFragment: Fragment?, isDownToUp: Boolean
    ) {
        val transaction =
            mActivity.supportFragmentManager.beginTransaction()
        if (isDownToUp) {
            transaction.setCustomAnimations(
                R.animator.slide_fragment_vertical_right_in,
                R.animator.slide_fragment_vertical_left_out,
                R.animator.slide_fragment_vertical_left_in,
                R.animator.slide_fragment_vertical_right_out
            )

            //FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(mActivity, transaction, shooterFragment, targetedFragment, R.id.activity_menubar_containers);
            //fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_VERTICAL);
        } else {
            transaction.setCustomAnimations(
                R.animator.slide_fragment_horizontal_right_in,
                R.animator.slide_fragment_horizontal_left_out,
                R.animator.slide_fragment_horizontal_left_in,
                R.animator.slide_fragment_horizontal_right_out
            )

            //FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(mActivity, transaction, shooterFragment, targetedFragment, R.id.activity_menubar_containers);
            //fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_HORIZONTAL);
        }
        transaction.add(
            R.id.frameLayout,
            targetedFragment,
            targetedFragment.javaClass.simpleName
        )
        //curFragment = targetedFragment;
        transaction.hide(shooterFragment!!)
        transaction.addToBackStack(targetedFragment.javaClass.simpleName)
        transaction.commit()
    }


    fun restartApp(activity: Activity) {
        val intent = Intent(activity, SplashScreenActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }

    fun setLanguage(mContext: Context, langCode: String?) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLayoutDirection(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale)
        } else {
            setSystemLocaleLegacy(config, locale)
        }
        mContext.resources
            .updateConfiguration(config, mContext.resources.displayMetrics)
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun setSystemLocale(
        config: Configuration,
        locale: Locale?
    ) {
        config.setLocale(locale)
    }

    fun setSystemLocaleLegacy(
        config: Configuration,
        locale: Locale?
    ) {
        config.locale = locale
    }

    fun getDateTime(timeStamp: Long): String {
        try {
            val sdf = SimpleDateFormat("dd MMM yy hh:mm aa")
            val netDate = Date(timeStamp * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

}