package com.ensibuuko.android_dev_coding_assigment.api

import com.ensibuuko.android_dev_coding_assigment.data.CommentModel
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    companion object{
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("posts")
    suspend fun getPosts() : List<PostModel>

    @GET("/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId : Int) : List<CommentModel>
}