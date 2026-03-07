package com.artificialss.showcase

import androidx.room.RoomDatabase
import com.artificialss.showcase.data.local.AppDatabase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
