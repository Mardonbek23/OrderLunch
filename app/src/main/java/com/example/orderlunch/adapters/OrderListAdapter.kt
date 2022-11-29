package com.example.orderlunch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.orderlunch.R
import com.example.orderlunch.databinding.ItemMealBinding
import com.example.orderlunch.databinding.ItemOrderListBinding
import com.example.orderlunch.databinding.ItemOrdersBinding
import com.example.orderlunch.models.Meal
import com.example.orderlunch.models.Order
import com.example.orderlunch.models.OrderItem
import com.example.orderlunch.utils.hide
import com.example.orderlunch.utils.show


class OrderListAdapter(var list: List<OrderItem>, var listeners: OnClickListeners) :
    RecyclerView.Adapter<OrderListAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemOrderListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onClick(item: OrderItem, position: Int) {
            itemView.setOnClickListener {

            }
            binding.apply {

            }
        }

        fun onBind(item: OrderItem, position: Int) {
            binding.apply {
                name.text = item.name
                count.text = item.count.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemOrderListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onClick(list[position], position)
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListeners {
        fun onClick(position: Int, order: Order)
    }

}