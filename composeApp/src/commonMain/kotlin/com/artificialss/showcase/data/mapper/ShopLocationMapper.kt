package com.artificialss.showcase.data.mapper

import com.artificialss.showcase.data.local.entity.ShopLocationEntity
import com.artificialss.showcase.domain.model.ShopLocation

private const val IMAGE_SEPARATOR = "|"

fun ShopLocationEntity.toDomain(): ShopLocation = ShopLocation(
    id = id,
    name = name,
    category = category,
    latitude = latitude,
    longitude = longitude,
    rating = rating,
    address = address,
    hours = hours,
    images = images.split(IMAGE_SEPARATOR).filter { it.isNotBlank() },
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
    images = images.joinToString(IMAGE_SEPARATOR),
)
