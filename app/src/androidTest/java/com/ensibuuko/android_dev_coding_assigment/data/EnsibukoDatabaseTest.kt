package com.ensibuuko.android_dev_coding_assigment.data

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class EnsibukoDatabaseTest{
    private lateinit var db : EnsibukoDatabase
    private lateinit var dao : DaoInterface
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun createDb(){
        db = Room.inMemoryDatabaseBuilder(
            context, EnsibukoDatabase::class.java).build()
        dao = db.databaseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun writeAndReadPost() = runBlocking{
        val postModel = PostModel(1,1000,"test", "test")
        dao.insertSinglePost(postModel)
        val post = dao.fetchSinglePostById(postModel.id)
        assertThat(post, equalTo(postModel))
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadComment() = runBlocking {
        val commentModel = CommentModel(1000, 1000, "test", "testemail", "testbody")
        dao.insertSingleComment(commentModel)
        val comment = dao.fetchCommentById(commentModel.id)
        assertThat(comment, equalTo(commentModel))
    }
}