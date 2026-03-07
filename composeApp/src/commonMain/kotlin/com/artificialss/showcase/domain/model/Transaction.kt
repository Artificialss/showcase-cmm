package com.artificialss.showcase.domain.model

data class Transaction(
    val id: String,
    val amount: Double,
    val merchant: String,
    val category: TransactionCategory,
    val date: String,
)
