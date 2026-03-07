package com.artificialss.showcase.data.mapper

import com.artificialss.showcase.data.local.entity.ChartDataEntity
import com.artificialss.showcase.domain.model.ChartDataPoint
import com.artificialss.showcase.domain.model.ChartType

fun ChartDataEntity.toDomain(): ChartDataPoint = ChartDataPoint(
    id = id,
    label = label,
    value = value,
    type = ChartType.entries.firstOrNull { it.name == type } ?: ChartType.LINE,
    period = period,
)

fun ChartDataPoint.toEntity(): ChartDataEntity = ChartDataEntity(
    id = id,
    label = label,
    value = value,
    type = type.name,
    period = period,
)
