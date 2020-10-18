package com.darshan.darshanshop.views.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.FragmentProductDetailsBinding
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.models.viewmodels.ProductListViewModel
import com.darshan.darshanshop.utils.CommonMethods
import com.darshan.darshanshop.utils.Constants

class ProductDetailsFragment : BaseFragment() {

    private lateinit var binding : FragmentProductDetailsBinding
    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var strSelectedImagePath: String

    private lateinit var dialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_product_details,container,false)

        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)

        if(arguments != null){
            getProductFromDb(arguments!!.getString(Constants.PRODUCT_ID,"0"))
        }

        binding.floatPlaceBid.setOnClickListener(View.OnClickListener {
            openBidPlacerDialogue()
        })

        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            fragmentManager?.popBackStack()
        })

        return binding.root
    }

    private fun getProductFromDb(productId :String) {
        strSelectedImagePath =""
        productListViewModel.getProduct(productId).observe(this, Observer { product ->
            binding.tvProductName.setText(product.productName)
            strSelectedImagePath = product.productImgUrl
            Glide.with(DarshanShoppingApp.activityFromApp).load(strSelectedImagePath).into(binding.ivMain)
        })

        productListViewModel.getBidListing(productId).observe(this, Observer { bidList ->
            if(!bidList.isNullOrEmpty()) {
                binding.tvNoOfBids.setText("Total number of bids  : " + bidList.size)
                binding.tvAverageBid.setText("Average bid pricing  : $" + (bidList.fold(0.0) { total, item -> (total + item.bidPrice)}) / bidList.size)
                binding.tvHighest.setText("Highest bid pricing  : $" + bidList.maxBy { bid -> bid.bidPrice }!!.bidPrice)
                binding.tvLowest.setText("Lowest bid pricing  : $" + bidList.minBy { bid -> bid.bidPrice }!!.bidPrice)
            }
        })


    }

    private fun openBidPlacerDialogue() {
        val builder =
            AlertDialog.Builder(DarshanShoppingApp.activityFromApp)
        builder.setView(R.layout.dialogue_place_bid)
        dialog = builder.create()
        // display dialog
        dialog.show()
        dialog.setCancelable(true)
        val edBidAmount: EditText? = dialog.findViewById(R.id.edBidAMount)
        val tvPlaceBid: TextView? = dialog.findViewById(R.id.tvDone)

         tvPlaceBid?.setOnClickListener(View.OnClickListener {

             val bid = Bid(
                 bidId = null,
                 productId = arguments!!.getString(Constants.PRODUCT_ID,"0").toInt(),
                 bidPrice = CommonMethods.getTrimmedText(edBidAmount!!)!!.toDouble(),
                 bidTimeStamp = System.currentTimeMillis()
             )

             productListViewModel.saveBid(bid)

             dialog.dismiss()
         })

    }




}