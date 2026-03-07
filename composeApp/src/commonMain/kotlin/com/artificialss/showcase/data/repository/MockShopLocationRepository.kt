package com.artificialss.showcase.data.repository

import com.artificialss.showcase.data.local.dao.ShopLocationDao
import com.artificialss.showcase.data.mapper.toDomain
import com.artificialss.showcase.domain.model.ShopLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MockShopLocationRepository(
    private val shopLocationDao: ShopLocationDao,
) : ShopLocationRepository {

    override fun getShopLocations(): Flow<List<ShopLocation>> =
        shopLocationDao.getAll().map { entities -> entities.map { it.toDomain() } }
}
