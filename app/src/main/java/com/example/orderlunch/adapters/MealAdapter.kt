package com.example.orderlunch.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.orderlunch.R
import com.example.orderlunch.databinding.ItemMealBinding
import com.example.orderlunch.models.Meal
import com.example.orderlunch.utils.hide
import com.example.orderlunch.utils.show


class MealAdapter(var list: List<Meal>, var listeners: OnClickListeners, var isUser: Boolean) :
    RecyclerView.Adapter<MealAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onClick(meal: Meal, position: Int) {
            itemView.setOnClickListener {

            }
            binding.apply {
                btnOrder.setOnClickListener {
                    listeners.onOrder(position, meal)
                }
                llSwitch.setOnClickListener {
                    listeners.onNotHaveMeal(position, meal)
                }
            }
        }

        fun onBind(meal: Meal, position: Int) {
            binding.apply {
                name.text = meal.name
                if (meal.reason_nullable != null)
                    reason.show()
                reason.text = meal.reason_nullable.toString()
                if (meal.amount != null)
                    amount.show()
                amount.text = meal.amount.toString()
                if (meal.image != null) {
                    image.load(meal.image) {
                        crossfade(true)
                        crossfade(300)
                        placeholder(R.drawable.ic_image)
                    }
                }
                if (meal.isHave)
                    llContainer.setBackgroundColor(Color.WHITE)
                else
                    llContainer.setBackgroundColor(Color.parseColor("#FFF7B2"))
                switchMeal.isChecked = meal.isHave

                //USER
                if (isUser) {
                    btnOrder.show()
                    if (meal.isHave)
                        btnOrder.show()
                    else
                        btnOrder.hide()
                }
                //CHEF
                else {
                    switchMeal.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemMealBinding.inflate(
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
        fun onEdit(position: Int, meal: Meal)
        fun onClick(position: Int, meal: Meal)
        fun onOrder(position: Int, meal: Meal)
        fun onNotHaveMeal(position: Int, meal: Meal)
    }

}