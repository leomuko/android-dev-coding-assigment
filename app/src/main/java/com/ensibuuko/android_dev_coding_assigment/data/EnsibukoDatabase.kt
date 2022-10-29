package com.ensibuuko.android_dev_coding_assigment.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PostModel::class], version = 1)
abstract class EnsibukoDatabase : RoomDatabase(){

    abstract fun databaseDao() : DaoInterface
}