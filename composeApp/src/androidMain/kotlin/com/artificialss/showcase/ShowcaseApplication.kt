package com.artificialss.showcase

import android.app.Application
import com.artificialss.showcase.data.local.AppDatabase
import com.artificialss.showcase.data.local.DatabaseSeeder
import com.artificialss.showcase.di.appModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ShowcaseApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initializeAndroidContext(this)

        startKoin {
            androidContext(this@ShowcaseApplication)
            modules(platformModule() + appModules)
        }

        seedDatabase()
    }

    private fun seedDatabase() {
        val seeder: DatabaseSeeder by inject()
        applicationScope.launch {
            seeder.seedIfEmpty()
        }
    }
}

private fun platformModule() = module {
    single<AppDatabase> {
        getDatabaseBuilder()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
