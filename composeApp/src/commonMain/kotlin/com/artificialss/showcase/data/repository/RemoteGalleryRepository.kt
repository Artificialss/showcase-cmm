package com.artificialss.showcase.data.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.artificialss.showcase.domain.model.GalleryItem
import com.artificialss.showcase.graphql.GetGalleryPhotosQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteGalleryRepository(
    private val apolloClient: ApolloClient,
) : GalleryRepository {

    override fun getGalleryItems(): Flow<List<GalleryItem>> = flow {
        val result = runCatching {
            apolloClient
                .query(GetGalleryPhotosQuery(page = Optional.present(1), limit = Optional.present(DEFAULT_LIMIT)))
                .execute()
        }
        val items = result.getOrNull()
            ?.data
            ?.photos
            ?.data
            ?.mapNotNull { photo -> photo?.toDomain() }
            ?: emptyList()
        emit(items)
    }

    private fun GetGalleryPhotosQuery.Data1.toDomain(): GalleryItem = GalleryItem(
        id = id ?: "",
        imageUrl = url ?: "",
        thumbnailUrl = thumbnailUrl ?: "",
        title = title ?: "",
        albumTitle = album?.title ?: "",
    )

    companion object {
        private const val DEFAULT_LIMIT = 20
    }
}
