package com.example.minipromoter.repository

import androidx.lifecycle.LiveData
import com.example.minipromoter.App
import com.example.minipromoter.db.getDatabase
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.ProductModel
import com.example.minipromoter.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//
// Created by Abdul Basit on 3/8/2020.
//

class UserRepository {
    private val database = getDatabase(App.getInstance())


    fun getProductsList(): LiveData<List<ProductModel>> {
        return database.productDao.getAllProducts()
    }

    fun getProductsListWithOutLiveData(): List<ProductModel> {
        return database.productDao.getAllProductsWithOutLiveData()
    }

    fun getProductUsersList(produtName: String): LiveData<List<UserModel>> {
        return database.userDao.getProductUser(produtName)
    }

    fun getProductCampaigns(productId: Int): LiveData<List<Campaign>> {
        return database.campainDao.getProductCamapains(productId)
    }


    suspend fun insertProduct(name: String) {
        withContext(Dispatchers.IO) {
            val productModel = ProductModel(productName = name)
            database.productDao.insertProduct(productModel)
        }
    }

    suspend fun insertCampaign(campaign: Campaign) {
        withContext(Dispatchers.IO) {
            database.campainDao.insertCampain(campaign)
        }
    }

    suspend fun insertUser(userModel: UserModel) {
        withContext(Dispatchers.IO) {
            database.userDao.insertUser(userModel)
        }
    }
}