package com.ensibuuko.android_dev_coding_assigment.features.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.CommentModel
import com.ensibuuko.android_dev_coding_assigment.data.EnsibukoRepository
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: EnsibukoRepository,
    private val api: Api
) : ViewModel(){
     val commentsLiveData = MutableLiveData<List<CommentModel>>()
     var  postLiveData : LiveData<PostModel>? = null

    fun fetchComments(postId : Int){
        viewModelScope.launch {
            val comments = api.getComments(postId)
            commentsLiveData.value = comments
        }
    }

    fun fetchPostById(postId: Int){
        viewModelScope.launch {
            postLiveData = repository.getPostById(postId)
        }
    }
}