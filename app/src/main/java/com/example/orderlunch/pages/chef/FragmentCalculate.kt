package com.example.orderlunch.pages.chef

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.orderlunch.R
import com.example.orderlunch.adapters.MealAdapter
import com.example.orderlunch.databinding.DialogAddMealBinding
import com.example.orderlunch.databinding.FragmentCalculateBinding
import com.example.orderlunch.models.Meal
import com.example.orderlunch.utils.dialog_loading
import com.example.orderlunch.utils.hide
import com.google.firebase.database.*
import java.text.SimpleDateFormat

class FragmentCalculate : Fragment() {

    lateinit var binding: FragmentCalculateBinding

    var pattern = SimpleDateFormat("dd-MM-yyyy kk:mm")

    //realtime firebase
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    lateinit var mealAdapter: MealAdapter
    lateinit var list: ArrayList<Meal>
    var image: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculateBinding.inflate(inflater, container, false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("softBrains").child("meals")
        setBtns()
        setRv()
        return binding.root
    }

    private fun setRv() {
        list = ArrayList()
        mealAdapter = MealAdapter(list, object : MealAdapter.OnClickListeners {
            override fun onEdit(position: Int, meal: Meal) {

            }

            override fun onClick(position: Int, meal: Meal) {

            }

            override fun onOrder(position: Int, meal: Meal) {

            }
        }, false)
        binding.rv.adapter = mealAdapter
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        reference.addValueEventListener(object : ValueEventListener {
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
                list.reverse()
                mealAdapter.notifyDataSetChanged()
                binding.progress.hide()
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progress.hide()
            }
        })
    }

    private fun setBtns() {
        binding.apply {
            btnAdd.setOnClickListener {
                addMeal()
            }
        }
    }

    private fun addMeal() {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(requireContext())
        val customdialogBinding: DialogAddMealBinding =
            DialogAddMealBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(customdialogBinding.root)
        builder.setCancelable(false)
        val create = builder.create()
        customdialogBinding.cancel.setOnClickListener {
            create.dismiss()
        }
        customdialogBinding.save.setOnClickListener {
            val name = customdialogBinding.name.text
            if (name != null && name.length > 1) {
                val key = reference.push().key
                val currentTimeMillis = System.currentTimeMillis()
                val meal =
                    Meal(
                        key.toString(),
                        name.toString(),
                        currentTimeMillis,
                        customdialogBinding.description.text.toString(),
                        customdialogBinding.composition.text.toString(),
                        image,
                        customdialogBinding.amount.text.toString(),
                        pattern.format(currentTimeMillis)
                    )
                val dialogLoading =
                    requireContext().dialog_loading("", false, is_close_visibility = false)
                dialogLoading.show()
                reference.child(key!!).setValue(
                    meal
                ) { error, ref ->
                    dialogLoading.dismiss()
                    create.dismiss()
                    if (error == null) {
                        Toast.makeText(requireContext(), "Saqlandi!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Xatolik!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Taom nomi 2 va undan ortiq belgidan iborat bolishi kerak!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        create.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        create.show()
    }


}