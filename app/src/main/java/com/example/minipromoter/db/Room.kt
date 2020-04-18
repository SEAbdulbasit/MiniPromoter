package com.example.minipromoter.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minipromoter.models.Campaign
import com.example.minipromoter.models.CampaignMessages
import com.example.minipromoter.models.ProductModel
import com.example.minipromoter.models.UserModel

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUser(): LiveData<List<UserModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: UserModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserModel)

    @Query("SELECT * FROM users WHERE id LIKE :value LIMIT 1")
    fun getSingleUser(value: String): LiveData<UserModel>

    @Query("SELECT * FROM users WHERE productId = :value")
    fun getProductUser(value: Int): LiveData<List<UserModel>>
}


@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProductModel>>

    @Query("SELECT * FROM products")
    fun getAllProductsWithOutLiveData(): List<ProductModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProduct(vararg users: ProductModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(user: ProductModel): Long

}


@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaigns")
    fun getAllCampaigns(): LiveData<List<Campaign>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCampaigns(vararg users: Campaign)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampaign(campaign: Campaign)

    @Query("SELECT * FROM campaigns WHERE productId LIKE :value")
    fun getProductCampaigns(value: Int): LiveData<List<Campaign>>
}


@Dao
interface CampaignMessageDao {
    @Query("SELECT * FROM campaign_messages WHERE campaignId LIKE :value")
    fun getAllCampaignMessages(value: Int): LiveData<List<CampaignMessages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampaignMessage(campaignMessages: CampaignMessages)

    @Query("SELECT * FROM campaign_messages WHERE campaignMessageId LIKE :value")
    fun getCampaignMessage(value: Int): LiveData<List<CampaignMessages>>
}


@Database(
    entities = [UserModel::class, ProductModel::class, Campaign::class, CampaignMessages::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val productDao: ProductDao
    abstract val campaignDao: CampaignDao
    abstract val campaignMessageDao: CampaignMessageDao
}

private lateinit var INSTANCE: UserDatabase

fun getDatabase(context: Context): UserDatabase {
    synchronized(UserDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "mini_promoter_db"
            ).build()
        }
    }
    return INSTANCE
}