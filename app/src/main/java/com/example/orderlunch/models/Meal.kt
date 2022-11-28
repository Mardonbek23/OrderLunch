package com.example.orderlunch.models

class Meal {
    var id: String = ""
    var name: String = ""
    var create_date: Long = 0L
    var description: String? = null
    var composition: String? = null
    var image: String? = null
    var amount: String? = null
    var date_txt: String? = ""
    var count: Int? = 0
    var reason_nullable: String? = null
    var isHave: Boolean = true
    constructor()


    constructor(
        id: String,
        name: String,
        create_date: Long,
        isHave: Boolean,
        date_txt: String?,
        count: Int?,
        reason_nullable: String?,
        description: String?,
        composition: String?,
        amount: String?,
        image: String?
    ) {
        this.id = id
        this.name = name
        this.create_date = create_date
        this.isHave = isHave
        this.date_txt = date_txt
        this.count = count
        this.reason_nullable = reason_nullable
        this.description = description
        this.composition = composition
        this.amount = amount
        this.image = image
    }

    constructor(
        id: String,
        name: String,
        create_date: Long,
        description: String?,
        composition: String?,
        image: String?,
        amount: String?,
        date_txt: String?
    ) {
        this.id = id
        this.name = name
        this.create_date = create_date
        this.description = description
        this.composition = composition
        this.image = image
        this.amount = amount
        this.date_txt = date_txt
    }


}