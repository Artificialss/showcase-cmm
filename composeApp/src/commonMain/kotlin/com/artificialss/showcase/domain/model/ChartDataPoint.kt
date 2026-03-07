package com.artificialss.showcase.domain.model

data class ChartDataPoint(
    val id: String,
    val label: String,
    val value: Double,
    val type: ChartType,
    val period: String,
)

enum class ChartType {
    LINE,
    BAR,
}

enum class ChartPeriod(val label: String) {
    WEEK("This Week"),
    MONTH("This Month"),
    QUARTER("This Quarter"),
}
