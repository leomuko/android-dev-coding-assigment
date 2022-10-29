package com.ensibuuko.android_dev_coding_assigment.features.posts

import androidx.lifecycle.*
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.EnsibukoRepository
import com.ensibuuko.android_dev_coding_assigment.data.PostModel
import com.ensibuuko.android_dev_coding_assigment.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class PostsViewModel  @Inject constructor(
     private val repository: EnsibukoRepository,
     private val api: Api
) : ViewModel() {

    val posts = repository.getPosts().asLiveData()
    val makePostReply = MutableLiveData<PostModel>()

    fun fetchUserPosts(userId: Int) : LiveData<Resource<List<PostModel>>>{
        return repository.getUserPosts(userId).asLiveData()
    }


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