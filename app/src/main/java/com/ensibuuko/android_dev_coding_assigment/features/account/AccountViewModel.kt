package com.ensibuuko.android_dev_coding_assigment.features.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ensibuuko.android_dev_coding_assigment.api.Api
import com.ensibuuko.android_dev_coding_assigment.data.EnsibukoRepository
import com.ensibuuko.android_dev_coding_assigment.data.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel@Inject constructor(
    private val repository: EnsibukoRepository,
    private val api: Api
) : ViewModel(){

    val userLiveData = MutableLiveData<UserModel>()

    fun getUserData(userId : Int){
        viewModelScope.launch {
            userLiveData.value = api.getUser(userId)
        }
    }
}