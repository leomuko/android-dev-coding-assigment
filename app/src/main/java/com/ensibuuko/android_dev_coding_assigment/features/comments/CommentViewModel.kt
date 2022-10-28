package com.ensibuuko.android_dev_coding_assigment.features.comments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.CommentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val api: Api
) : ViewModel(){
     val commentsLiveData = MutableLiveData<List<CommentModel>>()


    fun fetchComments(postId : Int){
        viewModelScope.launch {
            val comments = api.getComments(postId)
            commentsLiveData.value = comments
        }
    }
}