package com.artificialss.showcase.data.repository

import com.artificialss.showcase.data.local.dao.GalleryItemDao
import com.artificialss.showcase.data.mapper.toDomain
import com.artificialss.showcase.domain.model.GalleryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MockGalleryRepository(
    private val galleryItemDao: GalleryItemDao,
) : GalleryRepository {

    override fun getGalleryItems(): Flow<List<GalleryItem>> =
        galleryItemDao.getAll().map { entities -> entities.map { it.toDomain() } }
}
