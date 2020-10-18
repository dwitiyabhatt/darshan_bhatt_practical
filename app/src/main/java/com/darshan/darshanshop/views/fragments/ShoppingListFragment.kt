package com.darshan.darshanshop.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.FragmentShoppingItemsBinding
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.models.viewmodels.ProductListViewModel
import com.darshan.darshanshop.utils.CommonMethods
import com.darshan.darshanshop.utils.Constants
import com.darshan.darshanshop.views.adapters.ShoppingItemsViewAdapter


class ShoppingListFragment : BaseFragment(), ShoppingItemsViewAdapter.Interaction {

    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var binding : FragmentShoppingItemsBinding
    private lateinit var shoppingItemsViewAdapter: ShoppingItemsViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_shopping_items,container,false)

        init()

        return binding.root
    }

    private fun init() {
        setViewModel()
        setRecyclerView()
        setUIAccordingToLogin()
        getDataFromDb()
        setClickListeners()
    }

    private fun setUIAccordingToLogin() {
        if (DarshanShoppingApp.sharedPreferences.getBoolean(Constants.IS_ADMIN, false)){
            binding.commonToolbar.tvTitle.setText(getString(R.string.title_welcome_admin))
            binding.fabSend.show()
        }
        else{
            binding.commonToolbar.tvTitle.setText(getString(R.string.title_welcom_user))
            binding.fabSend.hide()
        }

    }

    private fun setViewModel() {
        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
    }

    private fun setClickListeners() {
        binding.fabSend.setOnClickListener(View.OnClickListener {
            CommonMethods.addNextFragment(
                DarshanShoppingApp.activityFromApp,
                AddOrUpdateItemFragment(), this, false
            )

        })
        binding.commonToolbar.ivLogout.setOnClickListener(View.OnClickListener {
            showLogoutDialog()
        })
    }

    private fun getDataFromDb() {
        productListViewModel.getAllProductsList().observe(this, Observer { productsList ->
            if(productsList.isNullOrEmpty()){
                binding.rvShoopingList.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }else{
                binding.rvShoopingList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                shoppingItemsViewAdapter.submitList(productsList)
            }
        })
    }

    private fun setRecyclerView() {
        binding.rvShoopingList.apply {
            layoutManager = LinearLayoutManager(activity)
            shoppingItemsViewAdapter = ShoppingItemsViewAdapter(this@ShoppingListFragment)
            adapter = shoppingItemsViewAdapter
        }
    }


    override fun onItemSelected(position: Int, item: Product, view: View) {

        var bundle  = Bundle()
        bundle.putString(Constants.PRODUCT_ID,item.productId.toString())

        when(view.id){
            R.id.ivEdit ->{

                var addOrUpdateItemFragment  = AddOrUpdateItemFragment()
                addOrUpdateItemFragment.arguments = bundle
                CommonMethods.addNextFragment(DarshanShoppingApp.activityFromApp,
                    addOrUpdateItemFragment,this,false)

            }

                R.id.ivView ->{
                    if(DarshanShoppingApp.sharedPreferences.getBoolean(Constants.IS_ADMIN,false)){
                        var bidListFragment =  BidListFragment()
                        bidListFragment.arguments = bundle
                        CommonMethods.addNextFragment(DarshanShoppingApp.activityFromApp,
                            bidListFragment,this,false)
                    }else{
                        var productDetailsFragment =  ProductDetailsFragment()
                        productDetailsFragment.arguments = bundle
                        CommonMethods.addNextFragment(DarshanShoppingApp.activityFromApp,
                            productDetailsFragment,this,false)
                    }


                }

        }

        /*CommonMethods.addNextFragment(DarshanShoppingApp.activityFromApp,
            BidListFragment(),this,false)*/
    }

    private fun showLogoutDialog() {
        val builder =
            AlertDialog.Builder(DarshanShoppingApp.activityFromApp)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(
                    R.string.yes
                ) { dialogInterface, which ->
                    DarshanShoppingApp.clearPreferenceData()
                    CommonMethods.restartApp(DarshanShoppingApp.activityFromApp)
                }.setNegativeButton(
                    R.string.no
                ) { dialog, which -> dialog.dismiss() }
        builder.show()
    }


}