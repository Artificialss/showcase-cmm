package com.artificialss.showcase.data.mock

import com.artificialss.showcase.domain.model.ShopLocation
import kotlin.math.roundToInt
import kotlin.random.Random

object ShopLocationMockGenerator {

    private val RESTAURANT_NAMES = listOf(
        "La Pizzeria Roma", "Sushi Kyoto", "El Asador",
        "Café de Paris", "Thai Garden", "Burger Lab",
        "La Paella", "Pasta Fresca", "The Green Bowl", "Sweet Dreams",
    )

    private val CUISINES = listOf(
        "Italian", "Japanese", "Spanish Grill",
        "French Café", "Thai", "American",
        "Spanish Seafood", "Italian Pasta", "Healthy Bowls", "Desserts",
    )

    private val ADDRESSES = listOf(
        "Gran Via 32", "Calle de Serrano 47", "Calle Mayor 1",
        "Paseo de la Castellana 89", "Calle de Preciados 28", "Calle de Fuencarral 45",
        "Gran Via 15", "Calle de Goya 76", "Calle de Alcala 120", "Calle de Bravo Murillo 56",
    )

    private val HOURS = listOf(
        "12:00 - 23:00", "13:00 - 22:30", "13:00 - 23:00",
        "08:00 - 20:00", "12:00 - 22:00", "11:00 - 23:30",
        "13:00 - 22:00", "12:00 - 23:00", "09:00 - 21:00", "10:00 - 22:00",
    )

    private val RESTAURANT_IMAGES = listOf(
        listOf(
            "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1547592166-23ac45744acd?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1562802378-063ec186a863?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1617196034796-73dfa7b1fd56?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1484723091739-30a097e8f929?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1562565652-a0d8f0c59eb4?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1553979459-d2229ba7433b?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1565299507177-b0ac66763828?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1534080564583-6be75777b70a?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1565680018434-b513d5e5fd47?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1563379926898-05f4575a45d8?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1598866594230-a7c12756260f?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1490645935967-10de6ba17061?auto=format&w=400&q=80",
        ),
        listOf(
            "https://images.unsplash.com/photo-1551024506-0bccd828d307?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1563805042-7684c019e1cb?auto=format&w=400&q=80",
            "https://images.unsplash.com/photo-1501443762994-82bd5dace89a?auto=format&w=400&q=80",
        ),
    )

    fun generate(
        count: Int = DEFAULT_COUNT,
        seed: Long = 0L,
        centerLat: Double = MADRID_LAT,
        centerLng: Double = MADRID_LNG,
    ): List<ShopLocation> {
        val random = Random(seed)
        return List(count.coerceAtMost(RESTAURANT_NAMES.size)) { index ->
            val latOffset = (random.nextDouble() - OFFSET_CENTER) * SPREAD
            val lngOffset = (random.nextDouble() - OFFSET_CENTER) * SPREAD
            val rawRating = random.nextDouble(MIN_RATING, MAX_RATING)
            val rating = ((rawRating * RATING_FACTOR).roundToInt() / RATING_FACTOR).toFloat()
            ShopLocation(
                id = "restaurant_$index",
                name = RESTAURANT_NAMES[index],
                category = CUISINES[index],
                latitude = centerLat + latOffset,
                longitude = centerLng + lngOffset,
                rating = rating,
                address = ADDRESSES[index],
                hours = HOURS[index],
                images = RESTAURANT_IMAGES[index],
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
