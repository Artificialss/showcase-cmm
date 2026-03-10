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
    fun restaurantGeneratorIsDeterministic() {
        val first = ShopLocationMockGenerator.generate(seed = 789L)
        val second = ShopLocationMockGenerator.generate(seed = 789L)
        assertEquals(first, second)
    }

    @Test
    fun restaurantGeneratorHasNoBlankFields() {
        val restaurants = ShopLocationMockGenerator.generate(seed = 0L)
        restaurants.forEach { r ->
            assertTrue(r.id.isNotBlank())
            assertTrue(r.name.isNotBlank())
            assertTrue(r.category.isNotBlank())
            assertTrue(r.address.isNotBlank())
            assertTrue(r.hours.isNotBlank())
        }
    }

    @Test
    fun restaurantGeneratorHasThreeImagesEach() {
        val restaurants = ShopLocationMockGenerator.generate(seed = 0L)
        restaurants.forEach { r ->
            assertEquals(3, r.images.size, "Expected 3 images for ${r.name}")
        }
    }

    @Test
    fun restaurantImagesAreValidHttpsUrls() {
        val restaurants = ShopLocationMockGenerator.generate(seed = 0L)
        restaurants.forEach { r ->
            r.images.forEach { url ->
                assertTrue(url.startsWith("https://"), "Image URL must be HTTPS: $url")
            }
        }
    }

    @Test
    fun restaurantRatingsAreInValidRange() {
        val restaurants = ShopLocationMockGenerator.generate(seed = 0L)
        restaurants.forEach { r ->
            assertTrue(r.rating in 3.0f..5.0f, "Rating out of range for ${r.name}: ${r.rating}")
        }
    }

    @Test
    fun restaurantIdsHaveCorrectPrefix() {
        val restaurants = ShopLocationMockGenerator.generate(seed = 0L)
        restaurants.forEachIndexed { index, r ->
            assertEquals("restaurant_$index", r.id)
        }
    }

    @Test
    fun restaurantCountIsConstrainedByMaxCatalog() {
        val result = ShopLocationMockGenerator.generate(count = 100, seed = 0L)
        assertTrue(result.size <= 10, "Count should be capped at the catalog size (10)")
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
