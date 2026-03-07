package com.artificialss.showcase.di

import com.artificialss.showcase.data.repository.ChartRepository
import com.artificialss.showcase.data.repository.GalleryRepository
import com.artificialss.showcase.data.repository.MockChartRepository
import com.artificialss.showcase.data.repository.MockGalleryRepository
import com.artificialss.showcase.data.repository.MockShopLocationRepository
import com.artificialss.showcase.data.repository.MockTransactionRepository
import com.artificialss.showcase.data.repository.MockUserRepository
import com.artificialss.showcase.data.repository.ShopLocationRepository
import com.artificialss.showcase.data.repository.TransactionRepository
import com.artificialss.showcase.data.repository.UserRepository
import com.artificialss.showcase.ui.feature.analytics.AnalyticsPresenterImpl
import com.artificialss.showcase.ui.feature.dashboard.DashboardPresenterImpl
import com.artificialss.showcase.ui.feature.gallery.GalleryPresenterImpl
import com.artificialss.showcase.ui.feature.login.LoginPresenterImpl
import com.artificialss.showcase.ui.feature.map.MapPresenterImpl
import com.artificialss.showcase.ui.feature.profile.ProfilePresenterImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel { LoginPresenterImpl() }
}

val dashboardModule = module {
    single<TransactionRepository> { MockTransactionRepository(get()) }
    viewModel { DashboardPresenterImpl(get()) }
}

val analyticsModule = module {
    single<ChartRepository> { MockChartRepository(get()) }
    viewModel { AnalyticsPresenterImpl(get()) }
}

val mapModule = module {
    single<ShopLocationRepository> { MockShopLocationRepository(get()) }
    viewModel { MapPresenterImpl(get()) }
}

val galleryModule = module {
    single<GalleryRepository> { MockGalleryRepository(get()) }
    // Swap to go live with GraphQLZero:
    // single<GalleryRepository> { RemoteGalleryRepository(get()) }
    viewModel { GalleryPresenterImpl(get()) }
}

val profileModule = module {
    single<UserRepository> { MockUserRepository() }
    viewModel { ProfilePresenterImpl(get()) }
}
