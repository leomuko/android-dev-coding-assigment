package com.ensibuuko.android_dev_coding_assigment.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoInterface {


    //replace data from api with same id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(posts : List<PostModel>)

    //Delete Posts on Network Refresh
    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()

    //fetch Posts From Database
    @Query("SELECT * FROM posts")
    fun fetchAllPosts() : Flow<List<PostModel>>

    //fetch Post By Id
    @Query("SELECT * FROM posts WHERE id=:id")
    fun fetchPostById(id : Int) : LiveData<PostModel>


}