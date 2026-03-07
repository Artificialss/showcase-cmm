package com.artificialss.showcase.data.mapper

import com.artificialss.showcase.data.local.entity.ShopLocationEntity
import com.artificialss.showcase.domain.model.ShopLocation

fun ShopLocationEntity.toDomain(): ShopLocation = ShopLocation(
    id = id,
    name = name,
    category = category,
    latitude = latitude,
    longitude = longitude,
    rating = rating,
    address = address,
    hours = hours,
)

fun ShopLocation.toEntity(): ShopLocationEntity = ShopLocationEntity(
    id = id,
    name = name,
    category = category,
    latitude = latitude,
    longitude = longitude,
    rating = rating,
    address = address,
    hours = hours,
)
