package com.example.minipromoter.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


//
// Created by Abdul Basit on 3/8/2020.
//

@Entity(tableName = "products")
@Parcelize
data class ProductModel constructor(
    @PrimaryKey(autoGenerate = true)
    var productId: Int =0,
    val productName: String = "",
    val creatingTime: Long = System.currentTimeMillis()
) : Parcelable

@Entity(
    tableName = "users",
    foreignKeys = [ForeignKey(
        entity = ProductModel::class,
        parentColumns = arrayOf("productId"),
        childColumns = arrayOf("productId"),
        onDelete = CASCADE
    )]
)
class UserModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val phoneNumber: String,
    val subscriptionDate: Long = System.currentTimeMillis(),
    val productId: Int
)

@Entity(
    tableName = "campaigns",
    foreignKeys = [ForeignKey(
        entity = ProductModel::class,
        parentColumns = arrayOf("productId"),
        childColumns = arrayOf("productId"),
        onDelete = CASCADE
    )]
)
class Campaign(
    @PrimaryKey(autoGenerate = true)
    val campaignId: Int = 0,
    val campaignName: String,
    val campaignMessage: String,
    val campaignCreationDate: Long = System.currentTimeMillis(),
    val productId: Int
)