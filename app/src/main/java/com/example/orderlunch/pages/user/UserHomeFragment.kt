package com.example.orderlunch.pages.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.orderlunch.R
import com.example.orderlunch.adapters.MealAdapter
import com.example.orderlunch.adapters.OrderListAdapter
import com.example.orderlunch.adapters.OrdersAdapter
import com.example.orderlunch.databinding.DialogOrderDetailBinding
import com.example.orderlunch.databinding.DialogSendOrdersBinding
import com.example.orderlunch.databinding.FragmentUserHomeBinding
import com.example.orderlunch.models.Account
import com.example.orderlunch.models.Meal
import com.example.orderlunch.models.Order
import com.example.orderlunch.models.OrderItem
import com.example.orderlunch.utils.LocalData
import com.example.orderlunch.utils.hide
import com.example.orderlunch.utils.show
import com.google.firebase.database.*
import com.google.gson.Gson
import java.text.SimpleDateFormat


class UserHomeFragment : Fragment() {


    lateinit var binding: FragmentUserHomeBinding

    var pattern = SimpleDateFormat("EEE dd MMM")
    var patternOrder = SimpleDateFormat("dd_MM_yyyy")
    var patternFull = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    //realtime firebase
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var ordersAdapter: OrdersAdapter
    lateinit var list: ArrayList<Order>
    lateinit var account: Account
    lateinit var localData: LocalData
    lateinit var orderListAdapter: OrderListAdapter
    lateinit var orderItems: ArrayList<OrderItem>
    lateinit var customdialogBinding: DialogSendOrdersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("softBrains")
        localData = LocalData(requireContext())
        account = Gson().fromJson(localData.account(), Account::class.java)
        customdialogBinding =
            DialogSendOrdersBinding.inflate(LayoutInflater.from(requireContext()))

        setRv()
        setBtns()
        return binding.root
    }

    private fun setBtns() {
        binding.apply {
            send.setOnClickListener {
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(requireContext())

                builder.setView(customdialogBinding.root)
                builder.setCancelable(false)
                customdialogBinding.apply {
                    rvMeals.adapter = orderListAdapter
                }
                val create = builder.create()
                customdialogBinding.cancel.setOnClickListener {
                    create.dismiss()
                }
                create.show()
            }
        }
    }

    private fun setRv() {
        orderItems = ArrayList()
        list = ArrayList()
        ordersAdapter = OrdersAdapter(list, object : OrdersAdapter.OnClickListeners {
            override fun onClick(position: Int, order: Order) {

            }
        })

        orderListAdapter = OrderListAdapter(orderItems)
        binding.rv.adapter = ordersAdapter
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        reference.child("orders").child(patternOrder.format(System.currentTimeMillis()))
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    orderItems.clear()
                    for (child in snapshot.children) {
                        val value = child.getValue(Order::class.java)
                        if (value != null) {
                            list.add(value)
                            calculate(value)
                        }
                    }
                    orderListAdapter.list = orderItems
                    orderListAdapter.notifyDataSetChanged()
                    binding.progress.hide()
                    if (list.size == 0) {
                        binding.empty.show()
                    } else {
                        binding.empty.hide()
                    }
                    binding.count.text = list.size.toString()
                    ordersAdapter.notifyDataSetChanged()
                    customdialogBinding.cancel.text=list.size.toString()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun calculate(order: Order) {
        if (orderItems.size > 0) {
            var position = -1
            for (i in 0 until orderItems.size) {
                if (orderItems[i].mealId == order.meal!!.id) {
                    position = i
                    break
                }
            }
            if (position == -1) {
                orderItems.add(OrderItem(order.meal!!.id, order.meal!!.name, 1, order.meal!!.price))
            } else {
                orderItems[position].count++
            }
        } else {
            orderItems.add(OrderItem(order.meal!!.id, order.meal!!.name, 1, order.meal!!.price))
        }
    }
}