package com.artificialss.showcase.data.local

import com.artificialss.showcase.data.mapper.toEntity
import com.artificialss.showcase.data.mock.ChartMockGenerator
import com.artificialss.showcase.data.mock.GalleryMockGenerator
import com.artificialss.showcase.data.mock.ShopLocationMockGenerator
import com.artificialss.showcase.data.mock.TransactionMockGenerator

class DatabaseSeeder(private val database: AppDatabase) {

    suspend fun seedIfEmpty() {
        seedTransactions()
        seedChartData()
        seedShopLocations()
        seedGalleryItems()
    }

    private suspend fun seedTransactions() {
        if (database.transactionDao().count() > 0) return
        val entities = TransactionMockGenerator.generate(TRANSACTION_COUNT, SEED)
            .map { it.toEntity() }
        database.transactionDao().insertAll(entities)
    }

    private suspend fun seedChartData() {
        if (database.chartDataDao().count() > 0) return
        val entities = ChartMockGenerator.generate(SEED)
            .map { it.toEntity() }
        database.chartDataDao().insertAll(entities)
    }

    private suspend fun seedShopLocations() {
        if (database.shopLocationDao().count() > 0) return
        val entities = ShopLocationMockGenerator.generate(seed = SEED)
            .map { it.toEntity() }
        database.shopLocationDao().insertAll(entities)
    }

    private suspend fun seedGalleryItems() {
        if (database.galleryItemDao().count() > 0) return
        val entities = GalleryMockGenerator.generate(seed = SEED)
            .map { it.toEntity() }
        database.galleryItemDao().insertAll(entities)
    }

    companion object {
        private const val TRANSACTION_COUNT = 20
        private const val SEED = 42L
    }
}
