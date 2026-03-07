package com.artificialss.showcase

import com.artificialss.showcase.data.mock.ChartMockGenerator
import com.artificialss.showcase.data.mock.GalleryMockGenerator
import com.artificialss.showcase.data.mock.ShopLocationMockGenerator
import com.artificialss.showcase.data.mock.TransactionMockGenerator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MockGeneratorConsistencyTest {

    @Test
    fun transactionGeneratorIsDeterministic() {
        val first = TransactionMockGenerator.generate(count = 10, seed = 123L)
        val second = TransactionMockGenerator.generate(count = 10, seed = 123L)
        assertEquals(first, second)
    }

    @Test
    fun transactionGeneratorReturnsCorrectCount() {
        val result = TransactionMockGenerator.generate(count = 5, seed = 0L)
        assertEquals(5, result.size)
    }

    @Test
    fun chartGeneratorIsDeterministic() {
        val first = ChartMockGenerator.generate(seed = 456L)
        val second = ChartMockGenerator.generate(seed = 456L)
        assertEquals(first, second)
    }

    @Test
    fun chartGeneratorValuesAreNonNegative() {
        val data = ChartMockGenerator.generate(seed = 0L)
        assertTrue(data.all { it.value >= 0.0 })
    }

    @Test
    fun shopLocationGeneratorIsDeterministic() {
        val first = ShopLocationMockGenerator.generate(seed = 789L)
        val second = ShopLocationMockGenerator.generate(seed = 789L)
        assertEquals(first, second)
    }

    @Test
    fun shopLocationGeneratorHasNoNullFields() {
        val shops = ShopLocationMockGenerator.generate(seed = 0L)
        shops.forEach { shop ->
            assertTrue(shop.id.isNotBlank())
            assertTrue(shop.name.isNotBlank())
            assertTrue(shop.category.isNotBlank())
            assertTrue(shop.address.isNotBlank())
            assertTrue(shop.hours.isNotBlank())
        }
    }

    @Test
    fun galleryGeneratorReturnsValidUrls() {
        val items = GalleryMockGenerator.generate()
        items.forEach { item ->
            assertTrue(item.imageUrl.isNotBlank())
            assertTrue(item.imageUrl.startsWith("https://"))
        }
    }

    @Test
    fun galleryGeneratorRespectsCount() {
        val items = GalleryMockGenerator.generate(count = 5)
        assertEquals(5, items.size)
    }
}
