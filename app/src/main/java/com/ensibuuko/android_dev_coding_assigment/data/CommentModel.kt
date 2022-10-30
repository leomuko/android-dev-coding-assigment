package com.ensibuuko.android_dev_coding_assigment.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentModel (
    val postId : Int,
    @PrimaryKey val id : Int,
    val name : String,
    val email : String,
    var body : String
    )
