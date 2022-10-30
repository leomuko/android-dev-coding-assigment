package com.ensibuuko.android_dev_coding_assigment.features.comments

import androidx.lifecycle.*
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.CommentModel
import com.ensibuuko.android_dev_coding_assigment.data.EnsibukoRepository
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.util.Helpers
import com.ensibuuko.android_dev_coding_assigment.util.Helpers.Companion.createJsonRequestBody
import com.ensibuuko.android_dev_coding_assigment.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: EnsibukoRepository,
    private val api: Api
) : ViewModel(){
     //val commentsLiveData = MutableLiveData<List<CommentModel>>()
     var  postLiveData : LiveData<PostModel>? = null
    var commentsLiveData : LiveData<Resource<List<CommentModel>>>? = null
    val updatePostReply = MutableLiveData<PostModel>()
    val deletePostReply = MutableLiveData<Int>()
    val saveCommentReply = MutableLiveData<Long>()
    val deleteCommentReply = MutableLiveData<Int>()


    fun fetchComments(postId: Int){
        commentsLiveData =  repository.getComments(postId).asLiveData()
    }

    fun fetchPostById(postId: Int){
        viewModelScope.launch {
            postLiveData = repository.getPostById(postId)
        }
    }

    fun updatePost(title : String, body : String, userId : Int, postId: Int){
        viewModelScope.launch {
            val post = api.updateUserPost(
                createJsonRequestBody(
                    "id" to postId,
                    "title" to title,
                    "body" to body,
                    "userId" to userId
                )
            )
            repository.saveUserPosts(post)
            updatePostReply.value = post
        }
    }

    fun deletePost(post: PostModel){
        viewModelScope.launch {
            deletePostReply.value = repository.deletePostById(post)
        }
    }
    fun insertCommentInDb(commentModel: CommentModel){
        viewModelScope.launch {
            saveCommentReply.value = repository.insertNewComment(commentModel)
        }
    }

    fun deleteComment(commentModel: CommentModel){
        viewModelScope.launch {
            deleteCommentReply.value = repository.deleteComment(commentModel)
        }
    }

}