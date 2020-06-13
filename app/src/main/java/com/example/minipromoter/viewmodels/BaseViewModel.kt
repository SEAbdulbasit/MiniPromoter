package com.example.minipromoter.viewmodels




import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.minipromoter.App

open class BaseViewModel :
    AndroidViewModel(App.getInstance()) {

}