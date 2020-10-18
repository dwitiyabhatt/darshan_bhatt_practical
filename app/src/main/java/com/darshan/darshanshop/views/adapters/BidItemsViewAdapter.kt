package com.darshan.darshanshop.views.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.darshan.darshanshop.R
import com.darshan.darshanshop.databinding.RawBidBinding
import com.darshan.darshanshop.models.pojo.Bid
import com.darshan.darshanshop.utils.CommonMethods
import java.text.SimpleDateFormat
import java.util.*


class BidItemsViewAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Bid>() {

        override fun areItemsTheSame(oldItem: Bid, newItem: Bid): Boolean {
            return oldItem.bidId == newItem.bidId
        }

        override fun areContentsTheSame(oldItem: Bid, newItem: Bid): Boolean {
            return oldItem.equals(newItem)
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val mBinder: RawBidBinding
        mBinder = DataBindingUtil.inflate(
            LayoutInflater
                .from(parent.context), R.layout.raw_bid, parent, false
        )

        return BidItemVH(
            mBinder,
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BidItemVH -> {
                holder.bind(differ.currentList.get(position),position)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Bid>) {
        differ.submitList(list)
    }

    class BidItemVH constructor(
        binding: RawBidBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {
        var mBinder: RawBidBinding = binding

        fun bind(item: Bid, pos : Int) {
            mBinder.tvPrice.setText("Price : $"+item.bidPrice)
            mBinder.tvDateTime.setText("Bid time : "+ CommonMethods.getDateTime(item.bidTimeStamp))

            mBinder!!.tvSale.setOnClickListener {
                interaction?.onItemSelected(pos, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Bid)
    }


}
