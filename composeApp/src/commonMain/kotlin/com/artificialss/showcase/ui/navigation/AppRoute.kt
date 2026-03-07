package com.artificialss.showcase.ui.navigation

sealed interface AppRoute {
    data object Splash : AppRoute
    data object Login : AppRoute
    data object Dashboard : AppRoute
    data object Map : AppRoute
    data object Gallery : AppRoute
    data object Profile : AppRoute
    data object Components : AppRoute
}

val MAIN_ROUTES: List<AppRoute> = listOf(
    AppRoute.Dashboard,
    AppRoute.Map,
    AppRoute.Gallery,
    AppRoute.Profile,
    AppRoute.Components,
)
