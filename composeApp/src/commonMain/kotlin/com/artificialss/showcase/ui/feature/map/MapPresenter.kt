package com.artificialss.showcase.ui.feature.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artificialss.showcase.data.repository.ShopLocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

interface MapPresenter {
    val uiState: StateFlow<MapUiState>
    fun onShopSelected(shopId: String)
    fun onSheetDismissed()
}

class MapPresenterImpl(
    private val repository: ShopLocationRepository,
) : ViewModel(), MapPresenter {

    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    override val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var shops: List<com.artificialss.showcase.domain.model.ShopLocation> = emptyList()

    init {
        loadShops()
    }

    override fun onShopSelected(shopId: String) {
        val selected = shops.firstOrNull { it.id == shopId } ?: return
        _uiState.value = MapUiState.Success(shops = shops, selectedShop = selected)
    }

    override fun onSheetDismissed() {
        _uiState.value = MapUiState.Success(shops = shops, selectedShop = null)
    }

    private fun loadShops() {
        viewModelScope.launch {
            repository.getShopLocations()
                .catch { throwable ->
                    _uiState.value = MapUiState.Error(
                        throwable.message ?: "Failed to load shop locations",
                    )
                }
                .collect { locations ->
                    shops = locations
                    _uiState.value = MapUiState.Success(shops = locations, selectedShop = null)
                }
        }
    }
}
