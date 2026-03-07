package com.artificialss.showcase.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.artificialss.showcase.data.local.dao.ChartDataDao
import com.artificialss.showcase.data.local.dao.GalleryItemDao
import com.artificialss.showcase.data.local.dao.ShopLocationDao
import com.artificialss.showcase.data.local.dao.TransactionDao
import com.artificialss.showcase.data.local.entity.ChartDataEntity
import com.artificialss.showcase.data.local.entity.GalleryItemEntity
import com.artificialss.showcase.data.local.entity.ShopLocationEntity
import com.artificialss.showcase.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        ChartDataEntity::class,
        ShopLocationEntity::class,
        GalleryItemEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun chartDataDao(): ChartDataDao

    abstract fun shopLocationDao(): ShopLocationDao

    abstract fun galleryItemDao(): GalleryItemDao

    companion object {
        const val DATABASE_NAME = "showcase.db"
    }
}
