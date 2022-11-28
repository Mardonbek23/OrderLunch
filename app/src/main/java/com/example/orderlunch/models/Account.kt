package com.example.orderlunch.models

class Account {
    var id: String = ""
    var userName: String = ""
    var password: String = ""
    var createDate: Long = 0L
    var createDateTxt: String = "00"
    var role: Int = 0

    //USER-> 0
    //CHEF-> 1

    constructor()
    constructor(
        id: String,
        userName: String,
        password: String,
        createDate: Long,
        createDateTxt: String,
        role: Int
    ) {
        this.id = id
        this.userName = userName
        this.password = password
        this.createDate = createDate
        this.createDateTxt = createDateTxt
        this.role = role
    }


}