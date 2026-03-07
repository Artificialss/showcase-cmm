package com.artificialss.showcase

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artificialss.showcase.data.local.AppDatabase

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

private lateinit var appContext: Context

fun initializeAndroidContext(context: Context) {
    appContext = context.applicationContext
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = appContext.getDatabasePath(AppDatabase.DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
