package com.artificialss.showcase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.artificialss.showcase.ui.components.StyleBottomBar
import com.artificialss.showcase.ui.components.TopNavigationBar
import com.artificialss.showcase.ui.localization.LocalAppStrings
import com.artificialss.showcase.ui.localization.englishStrings
import com.artificialss.showcase.ui.localization.spanishStrings
import com.artificialss.showcase.ui.feature.analytics.AnalyticsPresenterImpl
import com.artificialss.showcase.ui.feature.components.ComponentsScreen
import com.artificialss.showcase.ui.feature.dashboard.DashboardPresenterImpl
import com.artificialss.showcase.ui.feature.dashboard.DashboardScreen
import com.artificialss.showcase.ui.feature.gallery.GalleryPresenterImpl
import com.artificialss.showcase.ui.feature.gallery.GalleryScreen
import com.artificialss.showcase.ui.feature.login.LoginPresenterImpl
import com.artificialss.showcase.ui.feature.login.LoginScreen
import com.artificialss.showcase.ui.feature.map.MapPresenterImpl
import com.artificialss.showcase.ui.feature.map.MapScreen
import com.artificialss.showcase.ui.feature.profile.ProfilePresenterImpl
import com.artificialss.showcase.ui.feature.profile.ProfileScreen
import com.artificialss.showcase.ui.feature.splash.SplashScreen
import com.artificialss.showcase.ui.navigation.AppRoute
import com.artificialss.showcase.ui.navigation.MAIN_ROUTES
import com.artificialss.showcase.ui.theme.AppStyleState
import com.artificialss.showcase.ui.theme.ColorPalette
import com.artificialss.showcase.ui.theme.FontStyle
import com.artificialss.showcase.ui.theme.ShapeStyle
import androidx.compose.foundation.isSystemInDarkTheme
import com.artificialss.showcase.ui.theme.ShowcaseTheme
import com.artificialss.showcase.ui.theme.ThemeVariant
import org.koin.compose.viewmodel.koinViewModel

private val ALL_ROUTES = listOf(
    AppRoute.Splash,
    AppRoute.Login,
    AppRoute.Dashboard,
    AppRoute.Map,
    AppRoute.Gallery,
    AppRoute.Profile,
    AppRoute.Components,
)

@Composable
fun App() {
    val systemDark = isSystemInDarkTheme()
    var themeOrdinal by rememberSaveable {
        mutableIntStateOf(if (systemDark) ThemeVariant.DARK.ordinal else ThemeVariant.LIGHT.ordinal)
    }
    var fontOrdinal by rememberSaveable { mutableIntStateOf(FontStyle.DEFAULT.ordinal) }
    var colorOrdinal by rememberSaveable { mutableIntStateOf(ColorPalette.GREEN.ordinal) }
    var shapeOrdinal by rememberSaveable { mutableIntStateOf(ShapeStyle.ROUNDED.ordinal) }
    var routeIndex by rememberSaveable { mutableIntStateOf(0) }
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    var showWelcomeDialog by rememberSaveable { mutableStateOf(false) }
    var isSpanish by rememberSaveable { mutableStateOf(false) }
    val strings = if (isSpanish) spanishStrings() else englishStrings()

    val appStyle = AppStyleState(
        themeVariant = ThemeVariant.entries[themeOrdinal],
        fontStyle = FontStyle.entries[fontOrdinal],
        colorPalette = ColorPalette.entries[colorOrdinal],
        shapeStyle = ShapeStyle.entries[shapeOrdinal],
    )
    val currentRoute = ALL_ROUTES[routeIndex]

    ShowcaseTheme(appStyle = appStyle) {
        CompositionLocalProvider(LocalAppStrings provides strings) {
        val showChrome = currentRoute in MAIN_ROUTES

        if (showWelcomeDialog) {
            WelcomeDialog(
                onDismiss = { showWelcomeDialog = false },
                onToggleLanguage = { isSpanish = !isSpanish },
            )
        }

        if (showChrome) {
            Scaffold(
                topBar = {
                    TopNavigationBar(
                        currentRoute = currentRoute,
                        onRouteSelected = { routeIndex = ALL_ROUTES.indexOf(it) },
                    )
                },
                bottomBar = {
                    StyleBottomBar(
                        appStyle = appStyle,
                        onStyleChanged = {
                            themeOrdinal = it.themeVariant.ordinal
                            fontOrdinal = it.fontStyle.ordinal
                            colorOrdinal = it.colorPalette.ordinal
                            shapeOrdinal = it.shapeStyle.ordinal
                        },
                    )
                },
                floatingActionButton = {
                    if (currentRoute == AppRoute.Dashboard || currentRoute == AppRoute.Map) {
                        FloatingActionButton(
                            onClick = { isSpanish = !isSpanish },
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Translate,
                                contentDescription = FAB_TRANSLATE_DESC,
                                modifier = Modifier.size(FAB_ICON_SIZE),
                            )
                        }
                    }
                },
            ) { innerPadding ->
                NavigationHost(
                    currentRoute = currentRoute,
                    isLoggedIn = isLoggedIn,
                    onRouteChanged = { routeIndex = ALL_ROUTES.indexOf(it) },
                    onLoginSuccess = {
                        isLoggedIn = true
                        showWelcomeDialog = true
                        routeIndex = ALL_ROUTES.indexOf(AppRoute.Dashboard)
                    },
                    shapeStyle = appStyle.shapeStyle,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        } else {
            NavigationHost(
                currentRoute = currentRoute,
                isLoggedIn = isLoggedIn,
                onRouteChanged = { routeIndex = ALL_ROUTES.indexOf(it) },
                onLoginSuccess = {
                    isLoggedIn = true
                    showWelcomeDialog = true
                    routeIndex = ALL_ROUTES.indexOf(AppRoute.Dashboard)
                },
                shapeStyle = appStyle.shapeStyle,
                modifier = Modifier.fillMaxSize(),
            )
        }
        } // CompositionLocalProvider
    }
}

@Composable
private fun WelcomeDialog(onDismiss: () -> Unit, onToggleLanguage: () -> Unit) {
    val s = LocalAppStrings.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DIALOG_PADDING),
            shape = RoundedCornerShape(DIALOG_CORNER_RADIUS),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = DIALOG_ELEVATION,
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DIALOG_CONTENT_PADDING),
                ) {
                    Spacer(modifier = Modifier.height(DIALOG_TRANSLATE_OFFSET))
                    Text(
                        text = s.welcomeTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(DIALOG_SPACING_XS))
                Text(
                    text = s.welcomeSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = DIALOG_SPACING_MD))

                SectionTitle(s.welcomeWhoWeAre)
                Spacer(modifier = Modifier.height(DIALOG_SPACING_XS))
                Text(
                    text = s.welcomeWhoWeAreText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(DIALOG_SPACING_MD))

                SectionTitle(s.welcomeHighlights)
                Spacer(modifier = Modifier.height(DIALOG_SPACING_XS))
                BulletPoint(s.welcomeHighlight1)
                BulletPoint(s.welcomeHighlight2)
                BulletPoint(s.welcomeHighlight3)
                BulletPoint(s.welcomeHighlight4)
                BulletPoint(s.welcomeHighlight5)

                Spacer(modifier = Modifier.height(DIALOG_SPACING_MD))

                Text(
                    text = s.welcomeThemeHint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(DIALOG_SPACING_MD))

                Text(
                    text = s.welcomeBuiltWith,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(DIALOG_SPACING_SM))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(s.welcomeExplore)
                    }
                }

                IconButton(
                    onClick = onToggleLanguage,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(DIALOG_TRANSLATE_PADDING),
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = FAB_TRANSLATE_DESC,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun BulletPoint(text: String) {
    Text(
        text = "  \u2022  $text",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = BULLET_VERTICAL_PADDING),
    )
}

@Composable
private fun NavigationHost(
    currentRoute: AppRoute,
    isLoggedIn: Boolean,
    onRouteChanged: (AppRoute) -> Unit,
    onLoginSuccess: () -> Unit,
    shapeStyle: ShapeStyle = ShapeStyle.ROUNDED,
    modifier: Modifier = Modifier,
) {
    when (currentRoute) {
        AppRoute.Splash -> {
            if (isLoggedIn) {
                LaunchedEffect(Unit) {
                    onRouteChanged(AppRoute.Dashboard)
                }
            } else {
                SplashScreen(
                    onNavigateToLogin = { onRouteChanged(AppRoute.Login) },
                    modifier = modifier,
                )
            }
        }
        AppRoute.Login -> {
            if (isLoggedIn) {
                LaunchedEffect(Unit) {
                    onRouteChanged(AppRoute.Dashboard)
                }
            } else {
                val presenter = koinViewModel<LoginPresenterImpl>()
                LoginScreen(
                    presenter = presenter,
                    onLoginSuccess = onLoginSuccess,
                    modifier = modifier,
                )
            }
        }
        AppRoute.Dashboard -> {
            val dashPresenter = koinViewModel<DashboardPresenterImpl>()
            val analyticsPresenter = koinViewModel<AnalyticsPresenterImpl>()
            DashboardScreen(
                dashboardPresenter = dashPresenter,
                analyticsPresenter = analyticsPresenter,
                modifier = modifier,
            )
        }
        AppRoute.Map -> {
            val presenter = koinViewModel<MapPresenterImpl>()
            MapScreen(presenter = presenter, modifier = modifier)
        }
        AppRoute.Gallery -> {
            val presenter = koinViewModel<GalleryPresenterImpl>()
            GalleryScreen(presenter = presenter, shapeStyle = shapeStyle, modifier = modifier)
        }
        AppRoute.Profile -> {
            val presenter = koinViewModel<ProfilePresenterImpl>()
            ProfileScreen(presenter = presenter, modifier = modifier)
        }
        AppRoute.Components -> ComponentsScreen(modifier = modifier)
    }
}

private val FAB_ICON_SIZE = 24.dp
private const val FAB_TRANSLATE_DESC = "Toggle language"
private val DIALOG_PADDING = 24.dp
private val DIALOG_CORNER_RADIUS = 20.dp
private val DIALOG_ELEVATION = 6.dp
private val DIALOG_CONTENT_PADDING = 24.dp
private val DIALOG_SPACING_XS = 4.dp
private val DIALOG_SPACING_SM = 8.dp
private val DIALOG_SPACING_MD = 12.dp
private val DIALOG_TRANSLATE_PADDING = 8.dp
private val DIALOG_TRANSLATE_OFFSET = 32.dp
private val BULLET_VERTICAL_PADDING = 2.dp
