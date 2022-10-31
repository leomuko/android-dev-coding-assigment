package com.ensibuuko.android_dev_coding_assigment.data


data class UserModel(
    val id : Int,
    val name : String,
    val username : String,
    val email : String,
    val address : AddressModel,
    val phone : String,
    val website: String,
    val company : CompanyModel
)

data class CompanyModel(
    val name: String,
    val catchPhrase : String,
    val bs : String
)

data class AddressModel(
    val street : String,
    val suite : String,
    val city : String,
    val zipcode : String,
    val geo: geo
)

data class geo(
    val lat : String,
    val lng : String,
)