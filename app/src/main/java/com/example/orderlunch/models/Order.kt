package com.example.orderlunch.models

class Order {
    var id: String = ""
    var meal: Meal? = null
    var user: String = ""
    var comment: String? = null
    var createDate: Long = 0L
    var createDatetxt: String = ""
    var updateDate: Long = 0L
    var updateDatetxt: String = ""

    constructor()
    constructor(
        id: String,
        meal: Meal?,
        user: String,
        comment: String?,
        createDate: Long,
        createDatetxt: String,
        updateDate: Long,
        updateDatetxt: String
    ) {
        this.id = id
        this.meal = meal
        this.user = user
        this.comment = comment
        this.createDate = createDate
        this.createDatetxt = createDatetxt
        this.updateDate = updateDate
        this.updateDatetxt = updateDatetxt
    }

}