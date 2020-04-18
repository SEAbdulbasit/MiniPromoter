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