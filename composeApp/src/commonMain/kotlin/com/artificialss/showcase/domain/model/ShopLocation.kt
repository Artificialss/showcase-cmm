package com.artificialss.showcase.domain.model

data class ShopLocation(
    val id: String,
    val name: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float,
    val address: String,
    val hours: String,
)
