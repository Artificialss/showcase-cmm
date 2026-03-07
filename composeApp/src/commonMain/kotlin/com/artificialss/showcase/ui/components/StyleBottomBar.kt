package com.artificialss.showcase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = navBarPadding.calculateBottomPadding())
                .padding(horizontal = HORIZONTAL_PADDING, vertical = VERTICAL_PADDING),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val isDark = appStyle.themeVariant == ThemeVariant.DARK
            StyleToggle(
                icon = Icons.Default.Refresh,
                label = if (isDark) "Dark" else "Light",
                onClick = {
                    val next = if (isDark) ThemeVariant.LIGHT else ThemeVariant.DARK
                    onStyleChanged(appStyle.copy(themeVariant = next))
                },
            )

            StyleToggle(
                icon = Icons.Default.Create,
                label = appStyle.fontStyle.name.lowercase().replaceFirstChar { it.uppercase() },
                onClick = {
                    val next = FontStyle.entries.let { entries ->
                        val idx = entries.indexOf(appStyle.fontStyle)
                        entries[(idx + 1) % entries.size]
                    }
                    onStyleChanged(appStyle.copy(fontStyle = next))
                },
            )

            StyleToggle(
                icon = Icons.Default.FavoriteBorder,
                label = appStyle.colorPalette.name.lowercase().replaceFirstChar { it.uppercase() },
                onClick = {
                    val next = ColorPalette.entries.let { entries ->
                        val idx = entries.indexOf(appStyle.colorPalette)
                        entries[(idx + 1) % entries.size]
                    }
                    onStyleChanged(appStyle.copy(colorPalette = next))
                },
            )
        }
        }
    }

@Composable
private fun StyleToggle(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
            modifier = Modifier
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
private val BADGE_HORIZONTAL_PADDING = 8.dp
private val BADGE_VERTICAL_PADDING = 2.dp
private const val BADGE_ALPHA = 0.2f
