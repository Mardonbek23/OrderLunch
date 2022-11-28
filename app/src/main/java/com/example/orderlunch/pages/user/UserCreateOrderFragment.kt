package com.example.orderlunch.pages.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.orderlunch.R
import com.example.orderlunch.adapters.MealAdapter
import com.example.orderlunch.databinding.FragmentUserCreateOrderBinding
import com.example.orderlunch.models.Meal
import com.example.orderlunch.utils.hide
import com.google.firebase.database.*
import java.text.SimpleDateFormat


class UserCreateOrderFragment : Fragment() {
    lateinit var binding: FragmentUserCreateOrderBinding

    var pattern = SimpleDateFormat("EEE dd MMM")
    var patternOrder = SimpleDateFormat("dd_MM_yyyy")

    //realtime firebase
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var mealAdapter: MealAdapter
    lateinit var list: ArrayList<Meal>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserCreateOrderBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("softBrains")
        setUi()
        setRv()
        return binding.root

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

                reference.child("orders").child(patternOrder.format(System.currentTimeMillis()))
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

}