package com.example.orderlunch.pages.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.orderlunch.R
import com.example.orderlunch.adapters.MealAdapter
import com.example.orderlunch.databinding.DialogAddMealBinding
import com.example.orderlunch.databinding.DialogOrderDetailBinding
import com.example.orderlunch.databinding.FragmentUserCreateOrderBinding
import com.example.orderlunch.models.Account
import com.example.orderlunch.models.Meal
import com.example.orderlunch.models.Order
import com.example.orderlunch.utils.LocalData
import com.example.orderlunch.utils.dialog_loading
import com.example.orderlunch.utils.hide
import com.google.firebase.database.*
import com.google.gson.Gson
import java.text.SimpleDateFormat


class UserCreateOrderFragment : Fragment() {
    lateinit var binding: FragmentUserCreateOrderBinding

    var pattern = SimpleDateFormat("EEE dd MMM")
    var patternOrder = SimpleDateFormat("dd_MM_yyyy")
    var patternFull = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    //realtime firebase
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var mealAdapter: MealAdapter
    lateinit var list: ArrayList<Meal>
    lateinit var account: Account
    lateinit var localData: LocalData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserCreateOrderBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("softBrains")
        localData = LocalData(requireContext())
        account = Gson().fromJson(localData.account(), Account::class.java)
        setUi()
        setRv()
        setMyOrder()
        return binding.root

    }

    private fun setMyOrder() {
        reference.child("orders").child(patternOrder.format(System.currentTimeMillis()))
            .child(account.id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(Order::class.java)
                    if (value != null) {
                        binding.tvOrder.text = value.name
                    }
                    else{
                        binding.tvOrder.text = "Tanlanmagan"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    @SuppressLint("SetTextI18n")
    private fun setUi() {
        binding.apply {
            tvDate.text = "Bugun(" + pattern.format(System.currentTimeMillis()) + "):"
        }
    }

    private fun setRv() {
        list = ArrayList()
        mealAdapter = MealAdapter(list, object : MealAdapter.OnClickListeners {
            override fun onEdit(position: Int, meal: Meal) {

            }

            override fun onClick(position: Int, meal: Meal) {

            }

            override fun onOrder(position: Int, meal: Meal) {
                orderMeal(meal)
            }


        }, true)
        binding.rv.adapter = mealAdapter
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        reference.child("meals").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                list.clear()
                for (child in children) {
                    val value = child.getValue(Meal::class.java)
                    if (value != null) {
                        list.add(value)
                    }
                }
                mealAdapter.notifyDataSetChanged()
                binding.progress.hide()
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progress.hide()
            }
        })
    }

    private fun orderMeal(meal: Meal) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(requireContext())
        val customdialogBinding: DialogOrderDetailBinding =
            DialogOrderDetailBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(customdialogBinding.root)
        builder.setCancelable(false)
        val create = builder.create()
        customdialogBinding.cancel.setOnClickListener {
            create.dismiss()
        }
        customdialogBinding.order.setOnClickListener {
            val currentTimeMillis = System.currentTimeMillis()
            val order = Order(
                account.id,
                meal.id,
                meal.name,
                account.userName,
                customdialogBinding.comment.text.toString(),
                currentTimeMillis,
                patternFull.format(currentTimeMillis),
                1L,
                ""
            )
            val dialogLoading = requireContext().dialog_loading(
                "Zakaz qilinmoqda...",
                is_cancellable = false,
                is_close_visibility = false
            )
            dialogLoading.show()
            reference.child("orders").child(patternOrder.format(System.currentTimeMillis()))
                .child(order.id).setValue(
                    order
                ) { error, ref ->
                    if (error == null) {
                        Toast.makeText(requireContext(), "Zakaz qilindi!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "Xatolik yuz berdi!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    dialogLoading.dismiss()
                    create.dismiss()
                }
        }
        create.show()
    }
}