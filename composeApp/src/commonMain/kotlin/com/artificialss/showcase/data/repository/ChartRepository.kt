package com.artificialss.showcase.data.repository

import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartType
import kotlinx.coroutines.flow.Flow

interface ChartRepository {

    fun getChartData(type: ChartType, period: String): Flow<List<ChartDataPoint>>
}
