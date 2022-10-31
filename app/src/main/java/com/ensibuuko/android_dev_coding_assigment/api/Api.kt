package com.ensibuuko.android_dev_coding_assigment.api

import com.ensibuuko.android_dev_coding_assigment.data.CommentModel
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.data.UserModel
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {

    companion object{
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("posts")
    suspend fun getPosts() : List<PostModel>

    @GET("/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId : Int) : List<CommentModel>

    @POST("/posts")
    suspend fun makePost(@Body params: RequestBody)  : PostModel

    @GET("/users/{userId}/posts")
    suspend fun getUserPosts(@Path("userId") userId : Int) : List<PostModel>

    @PUT("/posts/1")
    suspend fun updateUserPost(@Body params: RequestBody) : PostModel

    @DELETE("/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Int)

    @GET("/users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int) : UserModel
}