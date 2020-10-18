package com.darshan.darshanshop.views.adapters

import android.opengl.Visibility
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.darshan.darshanshop.DarshanShoppingApp
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.RawProductBinding
import com.darshan.darshanshop.models.pojo.Product
import com.darshan.darshanshop.utils.Constants
import kotlinx.android.synthetic.main.raw_product.view.*


class ShoppingItemsViewAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.equals(newItem)
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val mBinder: RawProductBinding
        mBinder = DataBindingUtil.inflate(
            LayoutInflater
                .from(parent.context), R.layout.raw_product, parent, false
        )

        return ShoppingItemVH(
            mBinder,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShoppingItemVH -> {
                holder.bind(differ.currentList.get(position),position)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }

    class ShoppingItemVH constructor(
        binding: RawProductBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        var mBinder: RawProductBinding = binding

        fun bind(item: Product, pos : Int) {
            mBinder.tvName.setText(item.productName)
            mBinder.tvPrice.setText("Price $"+item.productPrice)

            if (DarshanShoppingApp.sharedPreferences.getBoolean(Constants.IS_ADMIN, false)){
                mBinder.ivEdit.visibility = View.VISIBLE
            }else{
                mBinder.ivEdit.visibility = View.GONE
            }

            if(item.product_is_sold) {
                mBinder.tvSold.visibility = View.VISIBLE
            }else{
                mBinder.tvSold.visibility = View.GONE
            }

            Glide.with(itemView.context).load(item.productImgUrl).transform(
                CircleCrop()
            ).into(itemView.ivProfilePic)

            mBinder.ivEdit.setOnClickListener(View.OnClickListener {
                interaction?.onItemSelected(pos, item, mBinder.ivEdit)
            })

            mBinder.ivView.setOnClickListener(View.OnClickListener {
                interaction?.onItemSelected(pos, item, mBinder.ivView)
            })
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Product, view : View)
    }
}
