package com.example.minipromoter.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minipromoter.models.Campain
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

    @Query("SELECT * FROM users WHERE productId LIKE :value")
    fun getProductUser(value: String): LiveData<List<UserModel>>
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
    fun insertProduct(user: ProductModel)

}

@Dao
interface CampainDao {
    @Query("SELECT * FROM campains")
    fun getAllCamapins(): LiveData<List<Campain>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCampains(vararg users: Campain)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampain(campain: Campain)

    @Query("SELECT * FROM campains WHERE productId LIKE :value")
    fun getProductCamapains(value: String): LiveData<List<Campain>>
}

@Database(entities = [UserModel::class, ProductModel::class, Campain::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val productDao: ProductDao
    abstract val campainDao: CampainDao
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