package com.artificialss.showcase.data.repository

import com.artificialss.showcase.domain.model.GalleryItem
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {

    fun getGalleryItems(): Flow<List<GalleryItem>>
}
