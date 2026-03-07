package com.artificialss.showcase.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ShowcaseTheme(
    appStyle: AppStyleState = AppStyleState(),
    content: @Composable () -> Unit,
) {
    val colorScheme = colorSchemeFor(appStyle.colorPalette, appStyle.themeVariant)
    val typography = typographyForStyle(appStyle.fontStyle)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = ShowcaseShapes,
        content = content,
    )
}

private fun colorSchemeFor(palette: ColorPalette, variant: ThemeVariant): ColorScheme {
    val isDark = variant == ThemeVariant.DARK
    return when (palette) {
        ColorPalette.BLUE -> if (isDark) blueDarkScheme else blueLightScheme
        ColorPalette.GREEN -> if (isDark) greenDarkScheme else greenLightScheme
        ColorPalette.PURPLE -> if (isDark) purpleDarkScheme else purpleLightScheme
    }
}

private val blueLightScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = BlueTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
)

private val blueDarkScheme = darkColorScheme(
    primary = BluePrimaryDark,
    secondary = BlueSecondaryDark,
    tertiary = BlueTertiaryDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorDarkColor,
)

private val greenLightScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    tertiary = GreenTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
)

private val greenDarkScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    secondary = GreenSecondaryDark,
    tertiary = GreenTertiaryDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorDarkColor,
)

private val purpleLightScheme = lightColorScheme(
    primary = PurplePrimary,
    secondary = PurpleSecondary,
    tertiary = PurpleTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
)

private val purpleDarkScheme = darkColorScheme(
    primary = PurplePrimaryDark,
    secondary = PurpleSecondaryDark,
    tertiary = PurpleTertiaryDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorDarkColor,
)
