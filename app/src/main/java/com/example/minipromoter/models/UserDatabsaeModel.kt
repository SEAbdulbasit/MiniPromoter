package com.example.minipromoter.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


//
// Created by Abdul Basit on 3/8/2020.
//

@Entity(tableName = "products")
data class ProductModel constructor(
    @PrimaryKey
    val productName: String = "",
    val creatingTime: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "users",
    foreignKeys = [ForeignKey(
        entity = ProductModel::class,
        parentColumns = arrayOf("productName"),
        childColumns = arrayOf("productId"),
        onDelete = CASCADE
    )]
)
class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String,
    val subscriptionDate: Long = System.currentTimeMillis(),
    val productId: String
)

@Entity(
    tableName = "campains",
    foreignKeys = [ForeignKey(
        entity = ProductModel::class,
        parentColumns = arrayOf("productName"),
        childColumns = arrayOf("productId"),
        onDelete = CASCADE
    )]
)
class Campain(
    @PrimaryKey(autoGenerate = true)
    val campainId: Int = 0,
    val campainName: String,
    val campainMessage: String,
    val campainCreationDate: Long = System.currentTimeMillis(),
    val productId: String
)