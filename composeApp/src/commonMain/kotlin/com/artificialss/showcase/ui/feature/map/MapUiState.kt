package com.artificialss.showcase.ui.feature.map

import com.artificialss.showcase.domain.model.ShopLocation

sealed class MapUiState {
    data object Loading : MapUiState()

    data class Success(
        val shops: List<ShopLocation>,
        val selectedShop: ShopLocation?,
    ) : MapUiState()

    data class Error(val message: String) : MapUiState()
}
