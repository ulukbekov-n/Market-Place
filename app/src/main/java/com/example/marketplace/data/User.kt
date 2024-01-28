package com.example.marketplace.data

data class User(
    val firstName: String,
    val email:String,
    val imagePath: String =""
){
    constructor():this("","","")
}
