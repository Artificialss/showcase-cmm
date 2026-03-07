package com.artificialss.showcase.ui.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artificialss.showcase.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

interface DashboardPresenter {
    val uiState: StateFlow<DashboardUiState>
    fun onRefresh()
}

class DashboardPresenterImpl(
    private val repository: TransactionRepository,
) : ViewModel(), DashboardPresenter {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    override val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        onRefresh()
    }

    override fun onRefresh() {
        _uiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            repository.getTransactions()
                .catch { throwable ->
                    _uiState.value = DashboardUiState.Error(
                        throwable.message ?: "Failed to load transactions",
                    )
                }
                .collect { transactions ->
                    val spending = transactions.groupBy { it.category }
                        .mapValues { (_, txns) -> txns.sumOf { it.amount } }
                    _uiState.value = DashboardUiState.Success(
                        userName = MOCK_USER_NAME,
                        balance = MOCK_BALANCE,
                        cardNumber = MOCK_CARD_NUMBER,
                        transactions = transactions,
                        spendingByCategory = spending,
                    )
                }
        }
    }

    companion object {
        private const val MOCK_USER_NAME = "Elena Rodriguez"
        private const val MOCK_BALANCE = 24_850.75
        private const val MOCK_CARD_NUMBER = "**** **** **** 4832"
    }
}
