package com.artificialss.showcase.data.mock

import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartPeriod
import com.artificialss.showcase.domain.model.ChartType
import kotlin.math.roundToInt
import kotlin.random.Random

object ChartMockGenerator {

    private val WEEK_LABELS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val MONTH_LABELS = listOf(
        "Week 1", "Week 2", "Week 3", "Week 4",
    )
    private val QUARTER_LABELS = listOf("Jan", "Feb", "Mar")

    fun generate(seed: Long = 0L): List<ChartDataPoint> {
        val random = Random(seed)
        val points = mutableListOf<ChartDataPoint>()
        var idCounter = 0

        for (period in ChartPeriod.entries) {
            val labels = labelsForPeriod(period)
            for (chartType in ChartType.entries) {
                for (label in labels) {
                    val range = rangeForType(chartType)
                    val rawValue = random.nextDouble(range.first, range.second)
                    val value = (rawValue * ROUND_FACTOR).roundToInt() / ROUND_FACTOR
                    points.add(
                        ChartDataPoint(
                            id = "chart_$idCounter",
                            label = label,
                            value = value,
                            type = chartType,
                            period = period.name,
                        ),
                    )
                    idCounter++
                }
            }
        }

        return points
    }

    private fun labelsForPeriod(period: ChartPeriod): List<String> = when (period) {
        ChartPeriod.WEEK -> WEEK_LABELS
        ChartPeriod.MONTH -> MONTH_LABELS
        ChartPeriod.QUARTER -> QUARTER_LABELS
    }

    private fun rangeForType(type: ChartType): Pair<Double, Double> = when (type) {
        ChartType.LINE -> LINE_MIN to LINE_MAX
        ChartType.BAR -> BAR_MIN to BAR_MAX
    }

    private const val LINE_MIN = 1000.0
    private const val LINE_MAX = 15000.0
    private const val BAR_MIN = 20.0
    private const val BAR_MAX = 70.0
    private const val ROUND_FACTOR = 100.0
}
