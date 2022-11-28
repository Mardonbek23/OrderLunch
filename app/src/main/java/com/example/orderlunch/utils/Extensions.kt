package com.example.orderlunch.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.orderlunch.databinding.DialogLoadingBinding


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

//loading dialog
fun Context.dialog_loading(
    title: String,
    is_cancellable: Boolean,
    is_close_visibility: Boolean
): AlertDialog {
    val builder: AlertDialog.Builder =
        AlertDialog.Builder(this)
    val customdialogBinding: DialogLoadingBinding =
        DialogLoadingBinding.inflate(LayoutInflater.from(this))
    builder.setView(customdialogBinding.root)
    builder.setCancelable(is_cancellable)
    customdialogBinding.title.text = title
    if (title.isNotEmpty())
        customdialogBinding.title.show()
    if (is_close_visibility)
        customdialogBinding.btnClose.show()
    val create = builder.create()
    customdialogBinding.btnClose.setOnClickListener {
        create.dismiss()
    }
    create.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return create
}