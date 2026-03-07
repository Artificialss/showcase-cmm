package com.artificialss.showcase

import com.artificialss.showcase.data.local.entity.TransactionEntity
import com.artificialss.showcase.data.mapper.toDomain
import com.artificialss.showcase.data.mapper.toEntity
import com.artificialss.showcase.domain.model.Transaction
import com.artificialss.showcase.domain.model.TransactionCategory
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionMapperTest {

    @Test
    fun entityToDomainMapsCorrectly() {
        val entity = TransactionEntity(
            id = "txn_1",
            amount = 42.50,
            merchant = "Netflix",
            category = "ENTERTAINMENT",
            date = "2025-03-01",
        )
        val domain = entity.toDomain()
        assertEquals("txn_1", domain.id)
        assertEquals(42.50, domain.amount)
        assertEquals("Netflix", domain.merchant)
        assertEquals(TransactionCategory.ENTERTAINMENT, domain.category)
        assertEquals("2025-03-01", domain.date)
    }

    @Test
    fun domainToEntityMapsCorrectly() {
        val domain = Transaction(
            id = "txn_2",
            amount = 99.99,
            merchant = "Amazon",
            category = TransactionCategory.SHOPPING,
            date = "2025-03-15",
        )
        val entity = domain.toEntity()
        assertEquals("txn_2", entity.id)
        assertEquals(99.99, entity.amount)
        assertEquals("Amazon", entity.merchant)
        assertEquals("SHOPPING", entity.category)
        assertEquals("2025-03-15", entity.date)
    }

    @Test
    fun unknownCategoryFallsBackToOther() {
        val entity = TransactionEntity(
            id = "txn_3",
            amount = 10.0,
            merchant = "Unknown",
            category = "NONEXISTENT",
            date = "2025-01-01",
        )
        val domain = entity.toDomain()
        assertEquals(TransactionCategory.OTHER, domain.category)
    }

    @Test
    fun roundTripPreservesData() {
        val original = Transaction(
            id = "txn_4",
            amount = 123.45,
            merchant = "Starbucks",
            category = TransactionCategory.FOOD,
            date = "2025-06-20",
        )
        val roundTripped = original.toEntity().toDomain()
        assertEquals(original, roundTripped)
    }
}
