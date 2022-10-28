package com.ensibuuko.android_dev_coding_assigment.features.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel  @Inject constructor(
    api: Api
) : ViewModel() {

    private val postsLiveData = MutableLiveData<List<PostModel>>()
    val posts : LiveData<List<PostModel>> = postsLiveData

    init {
        viewModelScope.launch {
            val posts = api.getPosts()
            postsLiveData.value = posts
        }
    }
}