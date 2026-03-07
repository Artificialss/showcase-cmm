package com.artificialss.showcase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery_items")
data class GalleryItemEntity(
    @PrimaryKey val id: String,
    val imageUrl: String,
    val title: String,
)
