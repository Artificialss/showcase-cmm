package com.artificialss.showcase.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chart_data")
data class ChartDataEntity(
    @PrimaryKey val id: String,
    val label: String,
    val value: Double,
    val type: String,
    val period: String,
)
