package com.artificialss.showcase.data.repository

import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartPeriod

interface BitcoinRepository {

    suspend fun getBitcoinPrices(period: ChartPeriod): List<ChartDataPoint>
}
