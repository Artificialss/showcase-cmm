package com.artificialss.showcase.data.repository

import com.artificialss.showcase.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getTransactions(): Flow<List<Transaction>>
}
