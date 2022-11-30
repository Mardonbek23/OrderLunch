package com.example.orderlunch.pages.chef

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.orderlunch.R
import com.example.orderlunch.adapters.OrderListAdapter
import com.example.orderlunch.adapters.OrdersAdapter
import com.example.orderlunch.databinding.FragmentHomeBinding
import com.example.orderlunch.models.Account
import com.example.orderlunch.models.Order
import com.example.orderlunch.models.OrderItem
import com.example.orderlunch.utils.LocalData
import com.example.orderlunch.utils.currencyFormat
import com.example.orderlunch.utils.hide
import com.example.orderlunch.utils.show
import com.google.firebase.database.*
import com.google.gson.Gson
import java.text.SimpleDateFormat

class FragmentHome : Fragment() {

    lateinit var binding: FragmentHomeBinding

    var pattern = SimpleDateFormat("EEE dd MMM")
    var patternOrder = SimpleDateFormat("dd_MM_yyyy")
    var patternFull = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    //realtime firebase
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var ordersAdapter: OrdersAdapter
    lateinit var comments: ArrayList<Order>
    lateinit var orders: ArrayList<Order>
    lateinit var orderItems: ArrayList<OrderItem>
    lateinit var account: Account
    lateinit var localData: LocalData
    lateinit var orderListAdapter: OrderListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("softBrains")
        localData = LocalData(requireContext())
        account = Gson().fromJson(localData.account(), Account::class.java)



        setRv()
        return binding.root
    }

    private fun setRv() {
        comments = ArrayList()
        orders = ArrayList()
        orderItems = ArrayList()
        orderListAdapter = OrderListAdapter(orderItems)
        binding.rvOrders.adapter = orderListAdapter
        ordersAdapter = OrdersAdapter(comments, object : OrdersAdapter.OnClickListeners {
            override fun onClick(position: Int, order: Order) {

            }
        })
        binding.rvComments.adapter = ordersAdapter
        binding.rvComments.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        reference.child("orders").child(patternOrder.format(System.currentTimeMillis()))
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    comments.clear()
                    orders.clear()
                    orderItems.clear()
                    var sum = 0.0
                    var count = 0
                    for (child in snapshot.children) {
                        val value = child.getValue(Order::class.java)
                        if (value?.comment != null && value.comment.toString().length > 1) {
                            comments.add(value)
                        }
                        if (value != null) {
                            sum += value.meal!!.price
                            orders.add(value)
                            calculate(value)
                            if (value.meal!!.isPersonal)
                                count++
                        }
                    }
                    binding.price.text = "Summa: ${currencyFormat(sum)}so'm"
                    orderListAdapter.list = orderItems
                    orderListAdapter.notifyDataSetChanged()
                    binding.count.text = count.toString()
                    binding.progressComment.hide()
                    if (comments.size == 0) {
                        binding.emptyComment.show()
                    } else {
                        binding.emptyComment.hide()
                    }
                    ordersAdapter.notifyDataSetChanged()
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