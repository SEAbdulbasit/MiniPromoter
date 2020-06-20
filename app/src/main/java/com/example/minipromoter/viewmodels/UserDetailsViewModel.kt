package com.example.minipromoter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minipromoter.App
import com.example.minipromoter.Utils.Event
import com.example.minipromoter.models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserDetailsViewModel(userId: Long) : ViewModel() {

    //coroutine scope
    private var viewModelJob = Job()
    private var coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val userName = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val emailAddress = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val isActive = MutableLiveData(false)

    val userUpdated = MutableLiveData<Event<Unit>>()

    val user = App.getUserRepository().database.userDao.getUser(userId)

    fun userRetrieved(userModel: UserModel) {

        userName.value = userModel.username
        name.value = userModel.name
        emailAddress.value = userModel.email
        phoneNumber.value = userModel.phoneNumber
        isActive.value = userModel.isActivated

    }

    fun onUpdateClicked() {

        val userModel = user.value!!

        userModel.username = userName.value
        userModel.name = name.value
        userModel.email = emailAddress.value
        userModel.phoneNumber = phoneNumber.value
        userModel.isActivated = isActive.value
        userModel.updatedOn = System.currentTimeMillis()

        coroutineScope.launch(Dispatchers.IO) {
            App.getUserRepository().database.userDao.updateUser(userModel)
        }

        userUpdated.value = Event(Unit)

    }


    class Factory(val userId: Long) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserDetailsViewModel(userId = userId) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}