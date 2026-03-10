package com.artificialss.showcase.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle as ComposeItalic
import androidx.compose.ui.unit.sp

fun typographyForStyle(fontStyle: FontStyle): Typography {
    val fontFamily = when (fontStyle) {
        FontStyle.DEFAULT -> FontFamily.Default
        FontStyle.SERIF -> FontFamily.Serif
        FontStyle.MONOSPACE -> FontFamily.Monospace
        FontStyle.LIGHT -> FontFamily.SansSerif
        FontStyle.BOLD -> FontFamily.Default
        FontStyle.ITALIC -> FontFamily.Cursive
    }

    val italic = if (fontStyle == FontStyle.ITALIC) ComposeItalic.Italic else ComposeItalic.Normal

    val display = when (fontStyle) {
        FontStyle.LIGHT -> FontWeight.Light
        FontStyle.BOLD -> FontWeight.ExtraBold
        else -> FontWeight.Bold
    }
    val headline = when (fontStyle) {
        FontStyle.LIGHT -> FontWeight.ExtraLight
        FontStyle.BOLD -> FontWeight.Bold
        else -> FontWeight.SemiBold
    }
    val title = when (fontStyle) {
        FontStyle.LIGHT -> FontWeight.Light
        FontStyle.BOLD -> FontWeight.SemiBold
        else -> FontWeight.Medium
    }
    val body = when (fontStyle) {
        FontStyle.LIGHT -> FontWeight.ExtraLight
        FontStyle.BOLD -> FontWeight.Bold
        else -> FontWeight.Normal
    }
    val label = when (fontStyle) {
        FontStyle.LIGHT -> FontWeight.Light
        FontStyle.BOLD -> FontWeight.SemiBold
        else -> FontWeight.Medium
    }

    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = display,
            fontStyle = italic,
            fontSize = 57.sp,
            lineHeight = 64.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = display,
            fontStyle = italic,
            fontSize = 45.sp,
            lineHeight = 52.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = display,
            fontStyle = italic,
            fontSize = 36.sp,
            lineHeight = 44.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = headline,
            fontStyle = italic,
            fontSize = 32.sp,
            lineHeight = 40.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = headline,
            fontStyle = italic,
            fontSize = 28.sp,
            lineHeight = 36.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = headline,
            fontStyle = italic,
            fontSize = 24.sp,
            lineHeight = 32.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = title,
            fontStyle = italic,
            fontSize = 22.sp,
            lineHeight = 28.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = title,
            fontStyle = italic,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = title,
            fontStyle = italic,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = body,
            fontStyle = italic,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = body,
            fontStyle = italic,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = body,
            fontStyle = italic,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = label,
            fontStyle = italic,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = label,
            fontStyle = italic,
            fontSize = 12.sp,
            lineHeight = 16.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = label,
            fontStyle = italic,
            fontSize = 11.sp,
            lineHeight = 16.sp,
        ),
    )
}
