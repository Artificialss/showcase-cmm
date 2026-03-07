package com.artificialss.showcase.ui.feature.analytics

import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartPeriod

sealed class AnalyticsUiState {
    data object Loading : AnalyticsUiState()

    data class Success(
        val lineData: List<ChartDataPoint>,
        val barData: List<ChartDataPoint>,
        val selectedPeriod: ChartPeriod,
    ) : AnalyticsUiState()

    data class Error(val message: String) : AnalyticsUiState()
}
