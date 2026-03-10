package com.artificialss.showcase.data.repository

import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartPeriod
import com.artificialss.showcase.domain.model.ChartType
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class RemoteBitcoinRepository(
    private val httpClient: HttpClient,
) : BitcoinRepository {

    private val cache = mutableMapOf<ChartPeriod, List<ChartDataPoint>>()

    override suspend fun getBitcoinPrices(period: ChartPeriod): List<ChartDataPoint> {
        cache[period]?.let { return it }
        val days = periodToDays(period)
        val response = httpClient.get("$BASE_URL?vs_currency=usd&days=$days")
        val body = response.bodyAsText()
        val result = parsePrices(body, period)
        cache[period] = result
        return result
    }

    private fun periodToDays(period: ChartPeriod): Int = when (period) {
        ChartPeriod.WEEK -> DAYS_WEEK
        ChartPeriod.MONTH -> DAYS_MONTH
        ChartPeriod.QUARTER -> DAYS_QUARTER
    }

    private fun parsePrices(body: String, period: ChartPeriod): List<ChartDataPoint> {
        val root = JSON.parseToJsonElement(body).jsonObject
        val pricesArray = root["prices"]?.jsonArray ?: return emptyList()

        val allPrices = pricesArray.map { entry ->
            val pair = entry.jsonArray
            pair[INDEX_VALUE].jsonPrimitive.double
        }

        val labels = labelsForPeriod(period)
        return sampleToLabels(allPrices, labels)
    }

    private fun labelsForPeriod(period: ChartPeriod): List<String> = when (period) {
        ChartPeriod.WEEK -> WEEK_LABELS
        ChartPeriod.MONTH -> MONTH_LABELS
        ChartPeriod.QUARTER -> QUARTER_LABELS
    }

    private fun sampleToLabels(
        prices: List<Double>,
        labels: List<String>,
    ): List<ChartDataPoint> {
        if (prices.isEmpty()) return emptyList()
        val count = labels.size
        val step = (prices.size - 1).coerceAtLeast(1).toDouble() / (count - 1).coerceAtLeast(1)

        return labels.mapIndexed { index, label ->
            val sampleIndex = (index * step).toInt().coerceIn(0, prices.lastIndex)
            ChartDataPoint(
                id = "btc_$index",
                label = label,
                value = prices[sampleIndex],
                type = ChartType.LINE,
                period = label,
            )
        }
    }

    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3/coins/bitcoin/market_chart"
        private const val DAYS_WEEK = 7
        private const val DAYS_MONTH = 30
        private const val DAYS_QUARTER = 90
        private const val INDEX_VALUE = 1

        private val JSON = Json { ignoreUnknownKeys = true }

        private val WEEK_LABELS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        private val MONTH_LABELS = listOf("Week 1", "Week 2", "Week 3", "Week 4")
        private val QUARTER_LABELS = listOf("Jan", "Feb", "Mar")
    }
}
