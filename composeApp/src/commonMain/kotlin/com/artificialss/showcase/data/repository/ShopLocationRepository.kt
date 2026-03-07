package com.artificialss.showcase.data.repository

import com.artificialss.showcase.domain.model.ShopLocation
import kotlinx.coroutines.flow.Flow

interface ShopLocationRepository {

    fun getShopLocations(): Flow<List<ShopLocation>>
}
