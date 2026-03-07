package com.artificialss.showcase.data.mock

import com.artificialss.showcase.domain.model.Transaction
import com.artificialss.showcase.domain.model.TransactionCategory
import kotlin.math.roundToInt
import kotlin.random.Random

object TransactionMockGenerator {

    private val MERCHANTS = listOf(
        "Netflix", "Spotify", "Amazon", "Starbucks", "Apple",
        "Uber", "Zara", "Nike", "Whole Foods", "Steam",
        "Google Play", "Airbnb", "DoorDash", "Target", "Costco",
    )

    private val CATEGORY_BY_MERCHANT = mapOf(
        "Netflix" to TransactionCategory.ENTERTAINMENT,
        "Spotify" to TransactionCategory.ENTERTAINMENT,
        "Steam" to TransactionCategory.ENTERTAINMENT,
        "Amazon" to TransactionCategory.SHOPPING,
        "Zara" to TransactionCategory.SHOPPING,
        "Nike" to TransactionCategory.HEALTH,
        "Target" to TransactionCategory.FOOD,
        "Costco" to TransactionCategory.FOOD,
        "Starbucks" to TransactionCategory.FOOD,
        "Whole Foods" to TransactionCategory.FOOD,
        "DoorDash" to TransactionCategory.FOOD,
        "Apple" to TransactionCategory.BILLS,
        "Google Play" to TransactionCategory.BILLS,
        "Uber" to TransactionCategory.TRANSPORT,
        "Airbnb" to TransactionCategory.TRAVEL,
    )

    fun generate(count: Int, seed: Long = 0L): List<Transaction> {
        val random = Random(seed)
        return List(count) { index ->
            val merchant = MERCHANTS[random.nextInt(MERCHANTS.size)]
            val rawAmount = random.nextDouble(MIN_AMOUNT, MAX_AMOUNT)
            val amount = (rawAmount * CENTS_FACTOR).roundToInt() / CENTS_FACTOR
            Transaction(
                id = "txn_$index",
                amount = amount,
                merchant = merchant,
                category = CATEGORY_BY_MERCHANT[merchant] ?: TransactionCategory.OTHER,
                date = "2025-03-${(index % DAYS_IN_MONTH + 1).toString().padStart(2, '0')}",
            )
        }
    }

    private const val MIN_AMOUNT = 2.0
    private const val MAX_AMOUNT = 500.0
    private const val CENTS_FACTOR = 100.0
    private const val DAYS_IN_MONTH = 28
}
