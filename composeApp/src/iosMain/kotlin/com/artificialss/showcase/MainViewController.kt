package com.artificialss.showcase

import androidx.compose.ui.window.ComposeUIViewController
import com.artificialss.showcase.data.local.AppDatabase
import com.artificialss.showcase.data.local.DatabaseSeeder
import com.artificialss.showcase.di.appModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() },
) {
    App()
}

private fun initializeKoin() {
    val platformModule = module {
        single<AppDatabase> {
            getDatabaseBuilder()
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }
    }

    val koinApp = startKoin {
        modules(listOf(platformModule) + appModules)
    }

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    scope.launch {
        val seeder = koinApp.koin.get<DatabaseSeeder>()
        seeder.seedIfEmpty()
    }
}
