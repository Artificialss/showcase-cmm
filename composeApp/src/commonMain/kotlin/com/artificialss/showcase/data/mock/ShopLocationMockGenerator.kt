package com.artificialss.showcase.data.mock

import com.artificialss.showcase.domain.model.ShopLocation
import kotlin.math.roundToInt
import kotlin.random.Random

object ShopLocationMockGenerator {

    private val SHOP_NAMES = listOf(
        "Casa del Libro", "El Corte Ingles", "Zara Home", "Mercadona",
        "Fnac", "Mango Outlet", "Primark", "Media Markt",
        "Decathlon", "Bershka",
    )

    private val CATEGORIES = listOf(
        "Books", "Department Store", "Home Decor", "Grocery",
        "Electronics", "Fashion", "Fashion", "Electronics",
        "Sports", "Fashion",
    )

    private val ADDRESSES = listOf(
        "Gran Via 32", "Calle de Serrano 47", "Calle Mayor 1", "Paseo de la Castellana 89",
        "Calle de Preciados 28", "Calle de Fuencarral 45", "Gran Via 15", "Calle de Goya 76",
        "Calle de Alcala 120", "Calle de Bravo Murillo 56",
    )

    fun generate(
        count: Int = DEFAULT_COUNT,
        seed: Long = 0L,
        centerLat: Double = MADRID_LAT,
        centerLng: Double = MADRID_LNG,
    ): List<ShopLocation> {
        val random = Random(seed)
        return List(count.coerceAtMost(SHOP_NAMES.size)) { index ->
            val latOffset = (random.nextDouble() - OFFSET_CENTER) * SPREAD
            val lngOffset = (random.nextDouble() - OFFSET_CENTER) * SPREAD
            val rawRating = random.nextDouble(MIN_RATING, MAX_RATING)
            val rating = ((rawRating * RATING_FACTOR).roundToInt() / RATING_FACTOR).toFloat()
            ShopLocation(
                id = "shop_$index",
                name = SHOP_NAMES[index],
                category = CATEGORIES[index],
                latitude = centerLat + latOffset,
                longitude = centerLng + lngOffset,
                rating = rating,
                address = ADDRESSES[index],
                hours = "10:00 - 21:00",
            )
        }
    }

    private const val DEFAULT_COUNT = 10
    private const val MADRID_LAT = 40.4168
    private const val MADRID_LNG = -3.7038
    private const val OFFSET_CENTER = 0.5
    private const val SPREAD = 0.03
    private const val MIN_RATING = 3.0
    private const val MAX_RATING = 5.0
    private const val RATING_FACTOR = 10.0
}
