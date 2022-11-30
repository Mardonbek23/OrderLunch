package com.example.orderlunch.models

data class OrderItem(
    var mealId: String = "",
    var name: String,
    var count: Int = 0,
    var price: Double? = 0.0
)