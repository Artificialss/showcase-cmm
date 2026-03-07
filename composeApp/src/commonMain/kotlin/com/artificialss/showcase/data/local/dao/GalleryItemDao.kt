package com.artificialss.showcase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artificialss.showcase.data.local.entity.GalleryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryItemDao {

    @Query("SELECT * FROM gallery_items")
    fun getAll(): Flow<List<GalleryItemEntity>>

    @Query("SELECT COUNT(*) FROM gallery_items")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<GalleryItemEntity>)
}
