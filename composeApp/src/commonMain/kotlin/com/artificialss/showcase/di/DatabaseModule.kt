package com.artificialss.showcase.di

import com.artificialss.showcase.data.local.AppDatabase
import com.artificialss.showcase.data.local.DatabaseSeeder
import org.koin.dsl.module

val databaseModule = module {
    single { get<AppDatabase>().transactionDao() }
    single { get<AppDatabase>().chartDataDao() }
    single { get<AppDatabase>().shopLocationDao() }
    single { get<AppDatabase>().galleryItemDao() }
    single { DatabaseSeeder(get()) }
}
