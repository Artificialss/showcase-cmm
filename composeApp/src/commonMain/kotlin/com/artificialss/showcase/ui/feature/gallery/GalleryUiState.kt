package com.artificialss.showcase.ui.feature.gallery

import com.artificialss.showcase.domain.model.GalleryItem

sealed class GalleryUiState {
    data object Loading : GalleryUiState()

    data class Success(
        val items: List<GalleryItem>,
        val selectedItem: GalleryItem?,
    ) : GalleryUiState()

    data class Error(val message: String) : GalleryUiState()
}
