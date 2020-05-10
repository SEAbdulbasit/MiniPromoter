package com.example.minipromoter.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minipromoter.fragment.ProductSubscribers
import com.example.minipromoter.models.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUser(): LiveData<List<UserModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: UserModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserModel): Long

    @Query("SELECT * FROM users WHERE userId LIKE :value LIMIT 1")
    fun getSingleUserWithOutLiveData(value: String): UserModel

    @Query("SELECT * FROM users WHERE phoneNumber LIKE :value LIMIT 1")
    fun findUserByPhoneNumber(value: String): UserModel?
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProductModel>>

    @Query("SELECT * FROM products")
    fun getAllProductsWithOutLiveData(): List<ProductModel>

    @Query("SELECT * FROM products WHERE productName LIKE :value LIMIT 1")
    fun searchForProduct(value: String): ProductModel

    @Query("SELECT * FROM products WHERE productId=:value LIMIT 1")
    fun getProductById(value: Long): ProductModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(vararg users: ProductModel)
}

@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaigns_table")
    fun getAllCampaigns(): LiveData<List<Campaign>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCampaigns(vararg users: Campaign)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampaign(campaign: Campaign): Long

    @Transaction
    @Query("SELECT * FROM campaigns_table WHERE productId=:value")
    fun getProductsCampaigns(value: Long): LiveData<List<Campaign>>

    @Query("SELECT * FROM campaigns_table WHERE campaignId=:value LIMIT 1")
    fun getCampaignById(value: Long): Campaign
}

@Dao
interface KeywordsDao {
    @Query("SELECT * FROM keywords_table")
    fun getAllKeywords(): LiveData<List<Keywords>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeywords(vararg keywords: Keywords)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKeywords(campaign: Keywords): Long

    @Query("SELECT * FROM keywords_table WHERE campaignId=:value")
    fun getCampaignKeywords(value: Long): LiveData<List<Keywords>>

    @Query("SELECT * FROM keywords_table WHERE description LIKE :value LIMIT 1")
    fun getKeywordByMessage(value: String): Keywords?
}

@Dao
interface CampaignMessageDao {
    @Query("SELECT * FROM campaign_messages")
    fun getAllCampaignMessages(): LiveData<List<CampaignMessages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampaignMessage(vararg campaignMessage: CampaignMessages)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampaignMessage(campaign: CampaignMessages): Long

    @Transaction
    @Query("SELECT * FROM campaign_messages WHERE messageId=:value")
    fun getCampaignMessage(value: Long): LiveData<List<CampaignMessages>>
}

@Dao
interface UserMessageDao {

    @Query("SELECT * FROM user_message")
    fun getAllMessages(): LiveData<List<UserMessage>>

    @Query("SELECT * FROM user_message where messageId=:value")
    fun getUserLastMessage(value: Long): UserMessage

    @Insert
    fun insertUserMessage(vararg userMessage: UserMessage)

    @Update
    fun updateUserMessage(userMessage: UserMessage)

    @Insert
    fun insertUserMessage(campaign: UserMessage): Long

    @Transaction
    @Query("SELECT * FROM user_message WHERE userId=:value")
    fun getAllUserMessages(value: Long): LiveData<List<UserMessage>>
}


@Dao
interface ProductSubscribersDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(userRepoJoin: SubscribersProductsCrossRef)

    @Update
    fun update(userRepoJoin: SubscribersProductsCrossRef)

    @Query("SELECT * FROM users INNER JOIN subscribers_products_table ON productId=subscribers_products_table.productId WHERE subscribers_products_table.productId=:productId")
    fun getProductSubscribers(productId: Long): LiveData<List<UserModel>>

    @Query("SELECT * FROM subscribers_products_table where productId LIKE :productId AND userId LIKE :userId ")
    fun getProductAndUserSubscriber(productId: Long, userId: Long): SubscribersProductsCrossRef?
}


/*


@Dao
interface CampaignMessageDao {
    @Query("SELECT * FROM campaign_messages WHERE campaignId LIKE :value")
    fun getAllCampaignMessages(value: Long): LiveData<List<CampaignMessages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampaignMessage(campaignMessages: CampaignMessages)

    @Query("SELECT * FROM campaign_messages WHERE campaignMessageId LIKE :value")
    fun getCampaignMessage(value: Long): LiveData<List<CampaignMessages>>
}

*/

@Database(
    entities = [UserModel::class, ProductModel::class, Campaign::class, Keywords::class, SubscribersProductsCrossRef::class, UserMessage::class, CampaignMessages::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val productDao: ProductDao
    abstract val campaignDao: CampaignDao
    abstract val keywordsDao: KeywordsDao
    abstract val campaignMessageDao: CampaignMessageDao
    abstract val productSubscribersDao: ProductSubscribersDao
    abstract val userMessageDao: UserMessageDao
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