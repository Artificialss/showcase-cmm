package com.artificialss.showcase.ui.feature.dashboard

import com.artificialss.showcase.domain.model.Transaction
import com.artificialss.showcase.domain.model.TransactionCategory

sealed class DashboardUiState {
    data object Loading : DashboardUiState()

    data class Success(
        val userName: String,
        val balance: Double,
        val cardNumber: String,
        val transactions: List<Transaction>,
        val spendingByCategory: Map<TransactionCategory, Double>,
    ) : DashboardUiState()

    data class Error(val message: String) : DashboardUiState()
}
