package com.ensibuuko.android_dev_coding_assigment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostModel(
    val userId : Int,
    @PrimaryKey val id : Int,
    val title : String,
    val body : String
)
