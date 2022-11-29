package com.example.orderlunch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.orderlunch.R
import com.example.orderlunch.databinding.ItemMealBinding
import com.example.orderlunch.databinding.ItemOrdersBinding
import com.example.orderlunch.models.Meal
import com.example.orderlunch.models.Order
import com.example.orderlunch.utils.hide
import com.example.orderlunch.utils.show


class OrdersAdapter(var list: List<Order>, var listeners: OnClickListeners) :
    RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onClick(order: Order, position: Int) {
            itemView.setOnClickListener {

            }
            binding.apply {

            }
        }

        fun onBind(order: Order, position: Int) {
            binding.apply {
                user.text = order.user
                meal.text = order.name
                if (order.comment != null&& order.comment.toString().isNotEmpty()) {
                    llComment.show()
                    comment.text = order.comment
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemOrdersBinding.inflate(
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