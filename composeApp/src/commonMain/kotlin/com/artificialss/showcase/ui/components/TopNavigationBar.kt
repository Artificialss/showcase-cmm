package com.artificialss.showcase.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.artificialss.showcase.ui.navigation.AppRoute
import com.artificialss.showcase.ui.navigation.MAIN_ROUTES

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopNavigationBar(
    currentRoute: AppRoute,
    onRouteSelected: (AppRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = ELEVATION,
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = statusBarPadding.calculateTopPadding())
                .padding(horizontal = CONTENT_PADDING, vertical = VERTICAL_PADDING),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(ROW_SPACING),
        ) {
            MAIN_ROUTES.forEach { route ->
                val isSelected = route == currentRoute
                NavItem(
                    icon = iconFor(route),
                    label = labelFor(route),
                    isSelected = isSelected,
                    onClick = { onRouteSelected(route) },
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = SELECTED_BG_ALPHA)
    } else {
        MaterialTheme.colorScheme.primary
    }
    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = UNSELECTED_CONTENT_ALPHA)
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(ITEM_CORNER_RADIUS))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = ITEM_HORIZONTAL_PADDING, vertical = ITEM_VERTICAL_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(ICON_SIZE),
        )
        Spacer(modifier = Modifier.height(LABEL_SPACING))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
        )
    }
}

private fun labelFor(route: AppRoute): String = when (route) {
    AppRoute.Dashboard -> NAV_HOME
    AppRoute.Map -> NAV_MAP
    AppRoute.Gallery -> NAV_GALLERY
    AppRoute.Profile -> NAV_PROFILE
    AppRoute.Components -> NAV_UI_KIT
    else -> ""
}

private fun iconFor(route: AppRoute): ImageVector = when (route) {
    AppRoute.Dashboard -> Icons.Default.Home
    AppRoute.Map -> Icons.Default.LocationOn
    AppRoute.Gallery -> Icons.Outlined.Image
    AppRoute.Profile -> Icons.Default.AccountCircle
    AppRoute.Components -> Icons.Default.Build
    else -> Icons.Default.Home
}

private const val NAV_HOME = "Home"
private const val NAV_MAP = "Map"
private const val NAV_GALLERY = "Gallery"
private const val NAV_PROFILE = "Profile"
private const val NAV_UI_KIT = "UI Kit"
private val ELEVATION = 2.dp
private val CONTENT_PADDING = 8.dp
private val VERTICAL_PADDING = 8.dp
private val ROW_SPACING = 4.dp
private val ITEM_CORNER_RADIUS = 12.dp
private val ITEM_HORIZONTAL_PADDING = 12.dp
private val ITEM_VERTICAL_PADDING = 6.dp
private val ICON_SIZE = 22.dp
private val LABEL_SPACING = 2.dp
private const val SELECTED_BG_ALPHA = 0.25f
private const val UNSELECTED_CONTENT_ALPHA = 0.7f
