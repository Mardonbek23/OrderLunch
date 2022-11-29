package com.example.orderlunch.models

class Order {
    var id: String = ""
    var mealId: String = ""
    var name: String = ""
    var user: String = ""
    var comment: String? = null
    var createDate: Long = 0L
    var createDatetxt: String = ""
    var updateDate: Long = 0L
    var updateDatetxt: String = ""

    constructor()
    constructor(
        id: String,
        mealId: String,
        name: String,
        user: String,
        comment: String?,
        createDate: Long,
        createDatetxt: String,
        updateDate: Long,
        updateDatetxt: String
    ) {
        this.id = id
        this.mealId = mealId
        this.name = name
        this.user = user
        this.comment = comment
        this.createDate = createDate
        this.createDatetxt = createDatetxt
        this.updateDate = updateDate
        this.updateDatetxt = updateDatetxt
    }

}