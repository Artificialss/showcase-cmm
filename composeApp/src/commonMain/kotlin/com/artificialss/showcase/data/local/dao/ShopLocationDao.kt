package com.artificialss.showcase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artificialss.showcase.data.local.entity.ShopLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopLocationDao {

    @Query("SELECT * FROM shop_locations")
    fun getAll(): Flow<List<ShopLocationEntity>>

    @Query("SELECT COUNT(*) FROM shop_locations")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ShopLocationEntity>)
}
