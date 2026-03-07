package com.artificialss.showcase.data.repository

import com.artificialss.showcase.data.local.dao.TransactionDao
import com.artificialss.showcase.data.mapper.toDomain
import com.artificialss.showcase.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MockTransactionRepository(
    private val transactionDao: TransactionDao,
) : TransactionRepository {

    override fun getTransactions(): Flow<List<Transaction>> =
        transactionDao.getAll().map { entities -> entities.map { it.toDomain() } }
}
