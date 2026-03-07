package com.artificialss.showcase.ui.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artificialss.showcase.data.repository.GalleryRepository
import com.artificialss.showcase.domain.model.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

interface GalleryPresenter {
    val uiState: StateFlow<GalleryUiState>
    fun onItemSelected(item: GalleryItem)
    fun onDismissViewer()
}

class GalleryPresenterImpl(
    private val repository: GalleryRepository,
) : ViewModel(), GalleryPresenter {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    override val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    private var items: List<GalleryItem> = emptyList()

    init {
        loadGallery()
    }

    override fun onItemSelected(item: GalleryItem) {
        _uiState.value = GalleryUiState.Success(items = items, selectedItem = item)
    }

    override fun onDismissViewer() {
        _uiState.value = GalleryUiState.Success(items = items, selectedItem = null)
    }

    private fun loadGallery() {
        viewModelScope.launch {
            repository.getGalleryItems()
                .catch { throwable ->
                    _uiState.value = GalleryUiState.Error(
                        throwable.message ?: "Failed to load gallery",
                    )
                }
                .collect { galleryItems ->
                    items = galleryItems
                    _uiState.value = GalleryUiState.Success(
                        items = galleryItems,
                        selectedItem = null,
                    )
                }
        }
    }
}
