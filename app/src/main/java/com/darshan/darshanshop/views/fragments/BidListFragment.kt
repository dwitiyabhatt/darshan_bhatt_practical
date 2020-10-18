package com.darshan.darshanshop.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.FragmentBidListBinding
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.models.viewmodels.ProductListViewModel
import com.darshan.darshanshop.utils.CommonMethods
import com.darshan.darshanshop.utils.Constants
import com.darshan.darshanshop.views.adapters.BidItemsViewAdapter

class BidListFragment : BaseFragment(), BidItemsViewAdapter.Interaction {

    private lateinit var binding : FragmentBidListBinding
    private lateinit var bidItemsViewAdapter: BidItemsViewAdapter
    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var  product : Product

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_bid_list,container,false)

        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        binding.commonToolbar.ivLogout.visibility = View.GONE

        if(arguments != null){
            getBidListFromDb(arguments!!.getString(Constants.PRODUCT_ID,"0"))

        }

        binding.rvBidList.apply {
            layoutManager = LinearLayoutManager(activity)
            bidItemsViewAdapter = BidItemsViewAdapter(this@BidListFragment)
            adapter = bidItemsViewAdapter
        }


        binding.commonToolbar.ivStart.setImageDrawable(ContextCompat.getDrawable(DarshanShoppingApp.activityFromApp,R.drawable.ic_back))
        binding.commonToolbar.ivStart.setOnClickListener(View.OnClickListener {
            fragmentManager!!.popBackStack()
        })

        binding.commonToolbar.tvTitle.setText(getString(R.string.title_bidding_list))

        return binding.root
    }

    /**
     * Feteches Data from Db and sets UI accordingly
     * */
    private fun getBidListFromDb(productId :String) {

        productListViewModel.getBidListing(productId).observe(this, Observer { bidList ->
            if(bidList.isNullOrEmpty()){
                binding.rvBidList.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }else{
                binding.rvBidList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                bidItemsViewAdapter.submitList(bidList)
            }

        })

        productListViewModel.getProduct(productId).observe(this, Observer { getproduct ->
            product = getproduct
        })


    }

    override fun onItemSelected(position: Int, item: Bid) {
                if(product.product_is_sold){
                    CommonMethods.showToast(DarshanShoppingApp.activityFromApp, getString(R.string.msg_product_sold))
                }else{
                    showConfirmationDialog()
                }

    }

    /**
     *  Shows Logout confirmation
     */
    private fun showConfirmationDialog() {
        val builder =
            AlertDialog.Builder(DarshanShoppingApp.activityFromApp)
                .setMessage(getString(R.string.msg_sell_item_confirmation))
                .setPositiveButton(
                    R.string.yes
                ) { dialogInterface, which ->
                    val id = arguments!!.getString(Constants.PRODUCT_ID,"0").toInt()

                    val product = Product(
                        productId = id,
                        productName = product.productName,
                        productPrice = product.productPrice,
                        productImgUrl = product.productImgUrl,
                        product_is_sold = true
                    )
                    productListViewModel.saveTodo(product)
                    CommonMethods.showToast(DarshanShoppingApp.activityFromApp, "Product sold successfully!")
                    fragmentManager!!.popBackStack()

                }.setNegativeButton(
                    R.string.no
                ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }
}