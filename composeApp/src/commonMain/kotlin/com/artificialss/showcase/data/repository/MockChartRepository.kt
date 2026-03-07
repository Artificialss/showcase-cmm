package com.artificialss.showcase.data.repository

import com.artificialss.showcase.data.local.dao.ChartDataDao
import com.artificialss.showcase.data.mapper.toDomain
import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MockChartRepository(
    private val chartDataDao: ChartDataDao,
) : ChartRepository {

    override fun getChartData(type: ChartType, period: String): Flow<List<ChartDataPoint>> =
        chartDataDao.getByTypeAndPeriod(type.name, period).map { entities ->
            entities.map { it.toDomain() }
        }
}
