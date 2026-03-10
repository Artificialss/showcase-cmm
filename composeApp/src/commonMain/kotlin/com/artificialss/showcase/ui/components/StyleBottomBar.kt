package com.artificialss.showcase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.artificialss.showcase.ui.theme.AppStyleState
import com.artificialss.showcase.ui.theme.ColorPalette
import com.artificialss.showcase.ui.theme.FontStyle
import com.artificialss.showcase.ui.theme.ShapeStyle
import com.artificialss.showcase.ui.theme.ThemeVariant

@Composable
fun StyleBottomBar(
    appStyle: AppStyleState,
    onStyleChanged: (AppStyleState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = ELEVATION,
    ) {
        BoxWithConstraints {
            val badgeWidth = if (maxWidth > maxHeight) BADGE_WIDTH_LANDSCAPE else BADGE_WIDTH_PORTRAIT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = navBarPadding.calculateBottomPadding())
                    .padding(horizontal = HORIZONTAL_PADDING, vertical = VERTICAL_PADDING),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ThemeToggle(appStyle = appStyle, onStyleChanged = onStyleChanged, badgeWidth = badgeWidth)
                FontToggle(appStyle = appStyle, onStyleChanged = onStyleChanged, badgeWidth = badgeWidth)
                ColorToggle(appStyle = appStyle, onStyleChanged = onStyleChanged, badgeWidth = badgeWidth)
                ShapeToggle(appStyle = appStyle, onStyleChanged = onStyleChanged, badgeWidth = badgeWidth)
            }
        }
    }
}

@Composable
private fun ThemeToggle(
    appStyle: AppStyleState,
    onStyleChanged: (AppStyleState) -> Unit,
    badgeWidth: Dp,
) {
    val isDark = appStyle.themeVariant == ThemeVariant.DARK
    StyleToggle(
        icon = Icons.Default.Refresh,
        label = if (isDark) LABEL_DARK else LABEL_LIGHT,
        badgeWidth = badgeWidth,
        onClick = {
            val next = if (isDark) ThemeVariant.LIGHT else ThemeVariant.DARK
            onStyleChanged(appStyle.copy(themeVariant = next))
        },
    )
}

@Composable
private fun FontToggle(
    appStyle: AppStyleState,
    onStyleChanged: (AppStyleState) -> Unit,
    badgeWidth: Dp,
) {
    StyleToggle(
        icon = Icons.Default.Create,
        label = appStyle.fontStyle.name.lowercase().replaceFirstChar { it.uppercase() },
        badgeWidth = badgeWidth,
        onClick = {
            val entries = FontStyle.entries
            val next = entries[(entries.indexOf(appStyle.fontStyle) + 1) % entries.size]
            onStyleChanged(appStyle.copy(fontStyle = next))
        },
    )
}

@Composable
private fun ColorToggle(
    appStyle: AppStyleState,
    onStyleChanged: (AppStyleState) -> Unit,
    badgeWidth: Dp,
) {
    StyleToggle(
        icon = Icons.Default.FavoriteBorder,
        label = appStyle.colorPalette.name.lowercase().replaceFirstChar { it.uppercase() },
        badgeWidth = badgeWidth,
        onClick = {
            val entries = ColorPalette.entries
            val next = entries[(entries.indexOf(appStyle.colorPalette) + 1) % entries.size]
            onStyleChanged(appStyle.copy(colorPalette = next))
        },
    )
}

@Composable
private fun ShapeToggle(
    appStyle: AppStyleState,
    onStyleChanged: (AppStyleState) -> Unit,
    badgeWidth: Dp,
) {
    val label = when (appStyle.shapeStyle) {
        ShapeStyle.ROUNDED -> LABEL_ROUNDED
        ShapeStyle.SQUARE -> LABEL_SQUARE
        ShapeStyle.PILL -> LABEL_PILL
    }
    StyleToggle(
        icon = Icons.Default.RoundedCorner,
        label = label,
        badgeWidth = badgeWidth,
        onClick = {
            val entries = ShapeStyle.entries
            val next = entries[(entries.indexOf(appStyle.shapeStyle) + 1) % entries.size]
            onStyleChanged(appStyle.copy(shapeStyle = next))
        },
    )
}

@Composable
private fun StyleToggle(
    icon: ImageVector,
    label: String,
    badgeWidth: Dp,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(indication = null, interactionSource = null, onClick = onClick),
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(BUTTON_SIZE),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(ICON_SIZE),
            )
        }
        Spacer(modifier = Modifier.width(BADGE_SPACING))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .width(badgeWidth)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = BADGE_ALPHA))
                .padding(horizontal = BADGE_HORIZONTAL_PADDING, vertical = BADGE_VERTICAL_PADDING),
        )
    }
}

private val ELEVATION = 2.dp
private val HORIZONTAL_PADDING = 4.dp
private val VERTICAL_PADDING = 4.dp
private val BUTTON_SIZE = 36.dp
private val ICON_SIZE = 20.dp
private val BADGE_SPACING = 2.dp
private val BADGE_WIDTH_PORTRAIT = 60.dp
private val BADGE_WIDTH_LANDSCAPE = 100.dp
private val BADGE_HORIZONTAL_PADDING = 5.dp
private val BADGE_VERTICAL_PADDING = 5.dp
private const val BADGE_ALPHA = 0.2f
private const val LABEL_DARK = "Dark"
private const val LABEL_LIGHT = "Light"
private const val LABEL_ROUNDED = "Round"
private const val LABEL_SQUARE = "Square"
private const val LABEL_PILL = "Pill"
