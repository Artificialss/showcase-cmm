package com.artificialss.showcase.ui.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artificialss.showcase.data.repository.ChartRepository
import com.artificialss.showcase.domain.model.ChartPeriod
import com.artificialss.showcase.domain.model.ChartType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

interface AnalyticsPresenter {
    val uiState: StateFlow<AnalyticsUiState>
    fun onPeriodSelected(period: ChartPeriod)
}

class AnalyticsPresenterImpl(
    private val chartRepository: ChartRepository,
) : ViewModel(), AnalyticsPresenter {

    private val _uiState = MutableStateFlow<AnalyticsUiState>(AnalyticsUiState.Loading)
    override val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    private var currentPeriod: ChartPeriod = ChartPeriod.WEEK

    init {
        loadData()
    }

    override fun onPeriodSelected(period: ChartPeriod) {
        currentPeriod = period
        loadData(showLoading = false)
    }

    private fun loadData(showLoading: Boolean = true) {
        if (showLoading) {
            _uiState.value = AnalyticsUiState.Loading
        }
        viewModelScope.launch {
            val lineFlow = chartRepository.getChartData(ChartType.LINE, currentPeriod.name)
            val barFlow = chartRepository.getChartData(ChartType.BAR, currentPeriod.name)

            combine(lineFlow, barFlow) { lineData, barData ->
                AnalyticsUiState.Success(
                    lineData = lineData,
                    barData = barData,
                    selectedPeriod = currentPeriod,
                )
            }
                .catch { throwable ->
                    _uiState.value = AnalyticsUiState.Error(
                        throwable.message ?: "Failed to load chart data",
                    )
                }
                .collect { successState ->
                    _uiState.value = successState
                }
        }
    }
}
