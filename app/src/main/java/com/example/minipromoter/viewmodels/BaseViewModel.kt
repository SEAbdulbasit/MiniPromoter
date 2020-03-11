package com.example.minipromoter.viewmodels

//
// Created by Abdul Basit on 2/17/2020.
// Copyright (c) 2020 VisionX. All rights reserved.
//


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.minipromoter.App

open class BaseViewModel :
    AndroidViewModel(App.getInstance()) {
    val showToastMessage = MutableLiveData<Boolean>(false)
    val toastMessage = MutableLiveData<String>("")
    val showProgressDialog = MutableLiveData<Boolean>(false)
    val cartItemsCount = MutableLiveData(0)
    val navigateToLoginScreen = MutableLiveData<Boolean>(false)


    internal fun transitionActivities(
        current: Context,
        newActivity: Class<*>,
        isFinished: Boolean
    ) {
        val intent = Intent(current, newActivity)
        current.startActivity(intent)
        if (isFinished) {
            (current as Activity).finish()
        }
    }

    internal fun transitionActivitiesBundle(
        current: Context,
        newActivity: Class<*>,
        bundle: Bundle?,
        isFinished: Boolean
    ) {
        val intent = Intent(current, newActivity)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        current.startActivity(intent)
        if (isFinished) {
            (current as Activity).finish()
        }
    }

    internal fun transitionActivitiesBundleWithFlags(
        current: Context,
        newActivity: Class<*>,
        bundle: Bundle,
        isFinished: Boolean,
        flags: List<Int>?
    ) {
        val intent = Intent(current, newActivity)
        intent.putExtras(bundle)
        if (!(flags == null || !flags.isNotEmpty())) {
            for (flag in flags) {
                intent.addFlags(flag)
            }
        }
        current.startActivity(intent)
        if (isFinished) {
            (current as Activity).finish()
        }
    }

    protected fun transitionActivitiesFlags(
        current: Context,
        newActivity: Class<*>,
        isFinished: Boolean,
        flags: List<Int>?
    ) {
        val intent = Intent(current, newActivity)
        if (!(flags == null || !flags.isNotEmpty())) {
            for (flag in flags) {
                intent.setFlags(flag)
            }
        }
        current.startActivity(intent)
        if (isFinished) {
            (current as Activity).finish()
        }
    }

    protected fun transitionAndFinishPreviousAcitivtes(
        current: Context,
        newActivity: Class<*>,
        _isFinished: Boolean
    ) {
        val intent = Intent(current, newActivity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        current.startActivity(intent)
    }

    protected fun showToast(message: String) {
        toastMessage.value = message
        showToastMessage.value = true
        //    Timber.d("\n Toast Message : " + toastMessage.value + " \n")
        dismissToastMessage()
    }

    fun dismissToastMessage() {
        showToastMessage.value = false
    }


}