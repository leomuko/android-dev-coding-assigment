package com.ensibuuko.android_dev_coding_assigment.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoInterface {


    //save posts to db
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

    //fetch Post By UserId
    @Query("SELECT * FROM posts WHERE userId=:userId")
    fun fetchUserPosts(userId : Int) : Flow<List<PostModel>>

    //Delete User Posts
    @Query("DELETE FROM posts WHERE userId=:userId")
    suspend fun deleteAllUserPosts(userId: Int)

    //save comments to db
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments : List<CommentModel>)

    //Delete Comments for specific post From Db
    @Query("DELETE FROM comments WHERE postId=:postId")
    suspend fun deleteAllPostComments(postId : Int)

    //fetch comments for specific post from db
    @Query("SELECT * FROM comments WHERE postId=:postId")
    fun fetchCommentsForPost(postId : Int) : Flow<List<CommentModel>>


}