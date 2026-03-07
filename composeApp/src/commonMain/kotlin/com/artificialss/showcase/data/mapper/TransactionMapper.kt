package com.artificialss.showcase.data.mapper

import com.artificialss.showcase.data.local.entity.TransactionEntity
import com.artificialss.showcase.domain.model.Transaction
import com.artificialss.showcase.domain.model.TransactionCategory

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    amount = amount,
    merchant = merchant,
    category = TransactionCategory.entries.firstOrNull { it.name == category }
        ?: TransactionCategory.OTHER,
    date = date,
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    amount = amount,
    merchant = merchant,
    category = category.name,
    date = date,
)
