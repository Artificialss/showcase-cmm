package com.artificialss.showcase.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artificialss.showcase.data.local.entity.ChartDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChartDataDao {

    @Query("SELECT * FROM chart_data WHERE type = :type AND period = :period ORDER BY label ASC")
    fun getByTypeAndPeriod(type: String, period: String): Flow<List<ChartDataEntity>>

    @Query("SELECT COUNT(*) FROM chart_data")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ChartDataEntity>)
}
