package com.ensibuuko.android_dev_coding_assigment.features.posts

import androidx.lifecycle.*
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.EnsibukoRepository
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PostsViewModel  @Inject constructor(
     repository: EnsibukoRepository,
     private val api: Api
) : ViewModel() {

    val posts = repository.getPosts().asLiveData()

//    private val postsLiveData = MutableLiveData<List<PostModel>>()
//    val posts : LiveData<List<PostModel>> = postsLiveData
    val makePostReply = MutableLiveData<PostModel>()

//    init {
//        viewModelScope.launch {
//            val posts = api.getPosts()
//            postsLiveData.value = posts
//        }
//    }

    fun makePost(title : String, body : String, userId : Int){
        viewModelScope.launch {
            val postMade = api.makePost(createJsonRequestBody("title" to title, "body" to body, "userId" to userId))
            makePostReply.value = postMade
        }

    }

    private fun createJsonRequestBody(vararg params: Pair<String, Any>) =
        RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            JSONObject(mapOf(*params)).toString())
}