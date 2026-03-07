package com.artificialss.showcase.data.mapper

import com.artificialss.showcase.data.local.entity.GalleryItemEntity
import com.artificialss.showcase.domain.model.GalleryItem

fun GalleryItemEntity.toDomain(): GalleryItem = GalleryItem(
    id = id,
    imageUrl = imageUrl,
    thumbnailUrl = imageUrl,
    title = title,
    albumTitle = "",
)

fun GalleryItem.toEntity(): GalleryItemEntity = GalleryItemEntity(
    id = id,
    imageUrl = imageUrl,
    title = title,
)
