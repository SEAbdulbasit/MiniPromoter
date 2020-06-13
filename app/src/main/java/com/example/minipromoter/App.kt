package com.example.minipromoter

import android.app.Application
import android.util.Log
import com.amitshekhar.DebugDB
import com.example.minipromoter.repository.UserRepository
import com.facebook.stetho.Stetho
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        Log.d("App", "DB Address: " + DebugDB.getAddressLog())

        Timber.plant(DebugTree())

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

object MessageType {
    const val CONVERSATION = "conversation"
    const val KEYWORD_MESSAGE = "keyword_message"

}