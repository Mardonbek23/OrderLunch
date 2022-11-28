package com.example.orderlunch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.orderlunch.databinding.ActivityMainBinding
import com.example.orderlunch.models.Account
import com.example.orderlunch.pages.chef.ChefActivity
import com.example.orderlunch.pages.user.UserActivity
import com.example.orderlunch.utils.LocalData
import com.example.orderlunch.utils.dialog_loading
import com.google.firebase.database.*
import com.google.gson.Gson
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var pattern = SimpleDateFormat("dd-MM-yyyy kk:mm")

    //realtime firebase
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("softBrains")
        if (LocalData(this).account() != null) {
            val fromJson = Gson().fromJson<Account>(LocalData(this).account(), Account::class.java)
            if (fromJson.role==0){
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this, ChefActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        setContentView(binding.root)
        setButtons()
    }

    private fun setButtons() {
        binding.btn.setOnClickListener {
            if (binding.name.text.toString().length < 4 || binding.password.text.toString().length < 3) {
                Toast.makeText(this, "Ma'lumotlarni to'ldiring!", Toast.LENGTH_SHORT).show()
            } else {
                val currentTimeMillis = System.currentTimeMillis()
                var account = Account(
                    reference.push().key.toString(),
                    binding.name.text.toString(),
                    binding.password.text.toString(),
                    currentTimeMillis,
                    pattern.format(currentTimeMillis),
                    0
                )
                if (binding.radio1.isChecked) {
                    account.role = 1
                    loginChef(account)
                } else if (binding.radio2.isChecked) {
                    account.role = 0
                    loginUser(account)
                } else
                    Toast.makeText(this, "Lavozimni tanlang!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun loginChef(account: Account) {
        val dialogLoading =
            this.dialog_loading("", is_cancellable = false, is_close_visibility = false)
        dialogLoading.show()
        reference.child("accounts").child("chefs")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isHave = false
                    for (child in snapshot.children) {
                        val value = child.getValue(Account::class.java)
                        if (value!!.userName == account.userName) {
                            isHave = true
                            break
                        }
                    }
                    if (isHave) {
                        if (dialogLoading.isShowing)
                            dialogLoading.dismiss()
                        Toast.makeText(
                            this@MainActivity,
                            "Bu foydalanuvchi mavjud!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        reference.child("accounts").child("chefs").child(account.id)
                            .setValue(account, object : DatabaseReference.CompletionListener {
                                override fun onComplete(
                                    error: DatabaseError?,
                                    ref: DatabaseReference
                                ) {
                                    if (dialogLoading.isShowing)
                                        dialogLoading.dismiss()
                                    if (error == null) {
                                        LocalData(this@MainActivity).account(Gson().toJson(account))
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                ChefActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Xatolik yuz berdi",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (dialogLoading.isShowing)
                        dialogLoading.dismiss()
                }
            })
    }

    fun loginUser(account: Account) {
        val dialogLoading =
            this.dialog_loading("", is_cancellable = false, is_close_visibility = false)
        dialogLoading.show()
        reference.child("accounts").child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var isHave = false
                    for (child in snapshot.children) {
                        val value = child.getValue(Account::class.java)
                        if (value!!.userName == account.userName) {
                            isHave = true
                            break
                        }
                    }
                    if (isHave) {
                        if (dialogLoading.isShowing)
                            dialogLoading.dismiss()
                        Toast.makeText(
                            this@MainActivity,
                            "Bu foydalanuvchi mavjud!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        reference.child("accounts").child("users").child(account.id)
                            .setValue(account, object : DatabaseReference.CompletionListener {
                                override fun onComplete(
                                    error: DatabaseError?,
                                    ref: DatabaseReference
                                ) {
                                    if (dialogLoading.isShowing)
                                        dialogLoading.dismiss()
                                    if (error == null) {
                                        LocalData(this@MainActivity).account(Gson().toJson(account))
                                        startActivity(
                                            Intent(
                                                this@MainActivity,
                                                UserActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Xatolik yuz berdi",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (dialogLoading.isShowing)
                        dialogLoading.dismiss()
                }
            })
    }
}