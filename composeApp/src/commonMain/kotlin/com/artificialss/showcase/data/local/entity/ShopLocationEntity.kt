package com.artificialss.showcase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_locations")
data class ShopLocationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float,
    val address: String,
    val hours: String,
    val images: String,
)
