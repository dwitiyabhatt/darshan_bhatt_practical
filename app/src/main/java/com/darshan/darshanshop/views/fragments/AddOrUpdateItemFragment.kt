package com.darshan.darshanshop.views.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.FragmentAddOrUpdateShoppingItemBinding
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.models.viewmodels.ProductListViewModel
import com.darshan.darshanshop.utils.CommonMethods
import com.darshan.darshanshop.utils.Constants
import kotlinx.android.synthetic.main.raw_product.view.*

class AddOrUpdateItemFragment : BaseFragment() {

    private lateinit var binding : FragmentAddOrUpdateShoppingItemBinding
    private var isEditMode = false
    private lateinit var strSelectedImagePath: String
    private lateinit var productListViewModel: ProductListViewModel




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_add_or_update_shopping_item,container,false)

        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        binding.commonToolbar.ivLogout.visibility = View.GONE

        if(arguments != null){
            isEditMode = true
            binding.commonToolbar.tvTitle.setText(getString(R.string.title_update_item))
            getProductFromDb(arguments!!.getString(Constants.PRODUCT_ID,"0"))
        }else{
            isEditMode = false
            binding.commonToolbar.tvTitle.setText(getString(R.string.title_add_item))
        }

        binding.commonToolbar.ivStart.setImageDrawable(ContextCompat.getDrawable(DarshanShoppingApp.activityFromApp,R.drawable.ic_back))

        binding.tvSubmit.setOnClickListener(View.OnClickListener {
            if(isValid()){
                addNewProduct()
            }

        })

        binding.ivProduct.setOnClickListener(View.OnClickListener {
            if(isPermissionsAllowed()) openGalleryForImage()
            else askForPermissions()

        })

        binding.commonToolbar.ivStart.setOnClickListener(View.OnClickListener {
            fragmentManager!!.popBackStack()
        })


        return binding.root
    }

    private fun getProductFromDb(productId :String) {
            productListViewModel.getProduct(productId).observe(this, Observer { product ->
            binding.edProductName.setText(product.productName)
            binding.edPrice.setText(product.productPrice.toString())
            strSelectedImagePath = product.productImgUrl
            Glide.with(DarshanShoppingApp.activityFromApp).load(strSelectedImagePath).transform(
                CircleCrop()
            ).into(binding.ivProduct)
        })


    }

    private fun addNewProduct() {

        val id = if (isEditMode) arguments!!.getString(Constants.PRODUCT_ID,"0").toInt() else null

        val product = Product(
            productId = id,
            productName = CommonMethods.getTrimmedText(binding.edProductName)!!,
            productPrice = CommonMethods.getTrimmedText(binding.edPrice)!!.toDouble(),
            productImgUrl = strSelectedImagePath,
            product_is_sold = false
        )

        productListViewModel.saveTodo(product)
        CommonMethods.showToast(DarshanShoppingApp.activityFromApp, "Product saved successfully!")
        fragmentManager!!.popBackStack()
    }

    private fun isValid() : Boolean{
        if(::strSelectedImagePath.isInitialized && strSelectedImagePath.isNullOrBlank()){
            CommonMethods.showToast(DarshanShoppingApp.activityFromApp,getString(R.string.msg_select_product_image))
            return false
        }
        else if(CommonMethods.getTrimmedText(binding.edProductName).isNullOrBlank()){
            CommonMethods.showToast(DarshanShoppingApp.activityFromApp,getString(R.string.msg_enter_product_name))
            return false
        }else if(CommonMethods.getTrimmedText(binding.edPrice).isNullOrBlank()){
            CommonMethods.showToast(DarshanShoppingApp.activityFromApp,getString(R.string.msg_enter_product_price))
            return false
        }
        return true
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.PICK_GALLERY_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_GALLERY_IMAGE){
           // binding.ivProduct.setImageURI(data?.data) // handle chosen image
            strSelectedImagePath = data?.data.toString()

            Glide.with(DarshanShoppingApp.activityFromApp).load(data?.data).transform(
                CircleCrop()
            ).into(binding.ivProduct)

        }
    }

    fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(DarshanShoppingApp.activityFromApp, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DarshanShoppingApp.activityFromApp,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat
                    .requestPermissions(DarshanShoppingApp.activityFromApp,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.REQUEST_EXTERNAL_STORAGE)
            }
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
        when (requestCode) {
            Constants.REQUEST_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                               openGalleryForImage()
                } else {
                    CommonMethods.showToast(DarshanShoppingApp.activityFromApp,getString(R.string.msg_allow_permission))
                }
                return
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(DarshanShoppingApp.activityFromApp)
            .setTitle(getString(R.string.msg_permission_denied))
            .setMessage(getString(R.string.msg_permission_from_settings))
            .setPositiveButton(getString(R.string.title_app_settings),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", DarshanShoppingApp.activityFromApp.getPackageName(), null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton("Cancel",null)
            .show()
    }


}