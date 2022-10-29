package com.ensibuuko.android_dev_coding_assigment.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.util.networkBoundResource
import javax.inject.Inject

class EnsibukoRepository @Inject constructor(
    private val api: Api,
    private val db : EnsibukoDatabase
) {
    private val dao = db.databaseDao()

    fun getPosts() = networkBoundResource(
        query = {
            dao.fetchAllPosts()
        },
        fetch = {
            api.getPosts()
        },
        saveFetchResult = {posts ->
            db.withTransaction {
                dao.deleteAllPosts()
                dao.insertPost(posts)
            }
        }
    )

    fun getUserPosts(userId : Int) = networkBoundResource(
        query = {
            dao.fetchUserPosts(userId)
        },
        fetch = {
            api.getUserPosts(userId)
        },
        saveFetchResult = {posts ->
            db.withTransaction {
                dao.deleteAllUserPosts(userId)
                dao.insertPost(posts)
            }
        }
    )

    fun getComments(postId : Int) = networkBoundResource(
        query = {
            dao.fetchCommentsForPost(postId)
        },
        fetch = {
            api.getComments(postId)
        },
        saveFetchResult = {comments ->
            db.withTransaction {
                dao.deleteAllPostComments(postId)
                dao.insertComments(comments)
            }
        }
    )

    fun getPostById(id : Int) : LiveData<PostModel> {
        dao.fetchPostById(id).value?.body?.let { Log.d("Repository", it) }
        return dao.fetchPostById(id)
    }
}