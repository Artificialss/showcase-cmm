package com.artificialss.showcase.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

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
        shapes = shapesFor(appStyle.shapeStyle),
        content = content,
    )
}

private fun colorSchemeFor(palette: ColorPalette, variant: ThemeVariant): ColorScheme {
    val isDark = variant == ThemeVariant.DARK
    return when (palette) {
        ColorPalette.BLUE -> if (isDark) blueDarkScheme else blueLightScheme
        ColorPalette.GREEN -> if (isDark) greenDarkScheme else greenLightScheme
        ColorPalette.PURPLE -> if (isDark) purpleDarkScheme else purpleLightScheme
        ColorPalette.ORANGE -> if (isDark) orangeDarkScheme else orangeLightScheme
        ColorPalette.GOLD -> if (isDark) goldDarkScheme else goldLightScheme
        ColorPalette.RED -> if (isDark) redDarkScheme else redLightScheme
    }
}

private val blueLightScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueContainerLight,
    onPrimaryContainer = OnBlueContainerLight,
    secondary = BlueSecondary,
    tertiary = BlueTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
    outline = BlueOutline,
)

private val blueDarkScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = OnBluePrimaryDark,
    primaryContainer = BlueContainerDark,
    onPrimaryContainer = OnBlueContainerDark,
    secondary = BlueSecondaryDark,
    tertiary = BlueTertiaryDark,
    surface = SurfaceDark,
    onSurface = Color.White,
    onBackground = Color.White,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorColor,
    outline = OutlineDark,
)

private val greenLightScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenContainerLight,
    onPrimaryContainer = OnGreenContainerLight,
    secondary = GreenSecondary,
    tertiary = GreenTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
    outline = GreenOutline,
)

private val greenDarkScheme = darkColorScheme(
    primary = GreenPrimary,
    onPrimary = Color.White,
    primaryContainer = GreenContainerDark,
    onPrimaryContainer = OnGreenContainerDark,
    secondary = GreenSecondaryDark,
    tertiary = GreenTertiary,
    surface = SurfaceDark,
    onSurface = Color.White,
    onBackground = Color.White,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorColor,
    outline = OutlineDark,
)

private val purpleLightScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleContainerLight,
    onPrimaryContainer = OnPurpleContainerLight,
    secondary = PurpleSecondary,
    tertiary = PurpleTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
    outline = PurpleOutline,
)

private val redLightScheme = lightColorScheme(
    primary = RedPrimary,
    onPrimary = Color.White,
    primaryContainer = RedContainerLight,
    onPrimaryContainer = OnRedContainerLight,
    secondary = RedSecondary,
    tertiary = RedTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
    outline = RedOutline,
)

private val redDarkScheme = darkColorScheme(
    primary = RedPrimaryDark,
    onPrimary = Color(0xFF690005),
    primaryContainer = RedContainerDark,
    onPrimaryContainer = OnRedContainerDark,
    secondary = RedSecondaryDark,
    tertiary = RedTertiaryDark,
    surface = SurfaceDark,
    onSurface = Color.White,
    onBackground = Color.White,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorColor,
    outline = OutlineDark,
)

private val goldLightScheme = lightColorScheme(
    primary = GoldPrimary,
    onPrimary = Color.White,
    primaryContainer = GoldContainerLight,
    onPrimaryContainer = OnGoldContainerLight,
    secondary = GoldSecondary,
    tertiary = GoldTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
    outline = GoldOutline,
)

private val goldDarkScheme = darkColorScheme(
    primary = GoldPrimaryDark,
    onPrimary = Color(0xFF3A2800),
    primaryContainer = GoldContainerDark,
    onPrimaryContainer = OnGoldContainerDark,
    secondary = GoldSecondaryDark,
    tertiary = GoldTertiaryDark,
    surface = SurfaceDark,
    onSurface = Color.White,
    onBackground = Color.White,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorColor,
    outline = OutlineDark,
)

private val orangeLightScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    primaryContainer = OrangeContainerLight,
    onPrimaryContainer = OnOrangeContainerLight,
    secondary = OrangeSecondary,
    tertiary = OrangeTertiary,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    background = BackgroundLight,
    error = ErrorColor,
    outline = OrangeOutline,
)

private val orangeDarkScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    primaryContainer = OrangeContainerDark,
    onPrimaryContainer = OnOrangeContainerDark,
    secondary = OrangeSecondaryDark,
    tertiary = OrangeTertiaryDark,
    surface = SurfaceDark,
    onSurface = Color.White,
    onBackground = Color.White,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorColor,
    outline = OutlineDark,
)

private val purpleDarkScheme = darkColorScheme(
    primary = PurplePrimaryDark,
    onPrimary = OnPurplePrimaryDark,
    primaryContainer = PurpleContainerDark,
    onPrimaryContainer = OnPurpleContainerDark,
    secondary = PurpleSecondaryDark,
    tertiary = PurpleTertiaryDark,
    surface = SurfaceDark,
    onSurface = Color.White,
    onBackground = Color.White,
    surfaceVariant = SurfaceVariantDark,
    background = BackgroundDark,
    error = ErrorColor,
    outline = OutlineDark,
)
