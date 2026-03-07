package com.artificialss.showcase

import com.artificialss.showcase.data.mock.TransactionMockGenerator
import com.artificialss.showcase.data.mapper.toEntity
import com.artificialss.showcase.data.repository.TransactionRepository
import com.artificialss.showcase.domain.model.Transaction
import com.artificialss.showcase.ui.feature.dashboard.DashboardPresenterImpl
import com.artificialss.showcase.ui.feature.dashboard.DashboardUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DashboardPresenterTest {

    @Test
    fun initialStateIsLoading() {
        val repository = FakeTransactionRepository(emptyList())
        val presenter = DashboardPresenterImpl(repository)
        assertTrue(
            presenter.uiState.value is DashboardUiState.Loading ||
                presenter.uiState.value is DashboardUiState.Success,
        )
    }

    @Test
    fun transactionsAreLoadedFromRepository() {
        val mockTransactions = TransactionMockGenerator.generate(count = 5, seed = 42L)
        val repository = FakeTransactionRepository(mockTransactions)
        val presenter = DashboardPresenterImpl(repository)
        presenter.onRefresh()
    }
}

private class FakeTransactionRepository(
    private val transactions: List<Transaction>,
) : TransactionRepository {
    override fun getTransactions(): Flow<List<Transaction>> = flowOf(transactions)
}
