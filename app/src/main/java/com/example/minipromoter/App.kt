package com.example.minipromoter

import android.app.Application
import com.example.minipromoter.repository.UserRepository

//
// Created by Abdul Basit on 2/17/2020.
// Copyright (c) 2020 VisionX. All rights reserved.
//


class App : Application() {

    override fun onCreate() {
        super.onCreate()

    }


    init {
        instance = this
    }


    companion object {
        private var instance: App? = null
        fun getInstance(): App {
            synchronized(App::class.java) {
                if (instance == null)
                    instance =
                        App()
            }
            return instance!!


            /*     }


                 private var moshi: Moshi? = null
                 fun getMoshi(): Moshi {
                     synchronized(App::class.java) {
                         if (moshi == null)
                             moshi = Moshi.Builder().build()
                     }
                     return moshi!!

                 }



                 private var responseHandler: app.px.packagex.services.network.ResponseHandler? = null
                 fun getResponseHandler(): app.px.packagex.services.network.ResponseHandler {
                     synchronized(App::class.java) {
                         if (responseHandler == null)
                             responseHandler = app.px.packagex.services.network.ResponseHandler()
                     }
                     return responseHandler!!

                 }*/


        }

        private var userRepository: UserRepository? = null
        fun getUserRepository(): UserRepository {
            synchronized(App::class.java) {
                if (userRepository == null)
                    userRepository = UserRepository()
            }
            return userRepository!!

        }


    }
}