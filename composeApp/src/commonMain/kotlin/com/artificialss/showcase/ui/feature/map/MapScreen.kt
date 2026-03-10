package com.artificialss.showcase.ui.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.artificialss.showcase.ui.localization.AppStrings
import com.artificialss.showcase.ui.localization.LocalAppStrings
import com.artificialss.showcase.domain.model.ShopLocation
import com.artificialss.showcase.ui.components.ErrorMessage
import com.artificialss.showcase.ui.components.LoadingIndicator
import com.artificialss.showcase.ui.components.map.CameraState
import com.artificialss.showcase.ui.components.map.MapMarker
import com.artificialss.showcase.ui.components.map.PlatformMapView

@Composable
fun MapScreen(
    presenter: MapPresenter,
    modifier: Modifier = Modifier,
) {
    val state by presenter.uiState.collectAsStateWithLifecycle()

    when (val current = state) {
        is MapUiState.Loading -> LoadingIndicator(modifier = modifier)
        is MapUiState.Error -> ErrorMessage(message = current.message, modifier = modifier)
        is MapUiState.Success -> MapContent(
            state = current,
            onShopSelected = { presenter.onShopSelected(it) },
            onSheetDismissed = { presenter.onSheetDismissed() },
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapContent(
    state: MapUiState.Success,
    onShopSelected: (String) -> Unit,
    onSheetDismissed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaultLat = if (state.shops.isNotEmpty()) state.shops.map { it.latitude }.average() else CameraState.DEFAULT_LAT
    val defaultLng = if (state.shops.isNotEmpty()) state.shops.map { it.longitude }.average() else CameraState.DEFAULT_LNG

    var cameraLat by rememberSaveable { mutableDoubleStateOf(defaultLat) }
    var cameraLng by rememberSaveable { mutableDoubleStateOf(defaultLng) }
    var cameraZoom by rememberSaveable { mutableFloatStateOf(CameraState.DEFAULT_ZOOM) }

    val markers = state.shops.map { shop ->
        MapMarker(
            id = shop.id,
            latitude = shop.latitude,
            longitude = shop.longitude,
            title = shop.name,
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        PlatformMapView(
            markers = markers,
            cameraState = CameraState(cameraLat, cameraLng, cameraZoom),
            onCameraMove = {
                cameraLat = it.latitude
                cameraLng = it.longitude
                cameraZoom = it.zoom
            },
            onMarkerClick = onShopSelected,
            modifier = Modifier.fillMaxSize(),
        )

        AiPoweredBadge(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = AI_BADGE_TOP_PADDING),
        )

        if (state.selectedShop != null) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = onSheetDismissed,
                sheetState = sheetState,
            ) {
                RestaurantDetailSheet(restaurant = state.selectedShop)
            }
        }
    }
}

@Composable
private fun RestaurantDetailSheet(restaurant: ShopLocation) {
    val s = LocalAppStrings.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SHEET_BOTTOM_PADDING),
    ) {
        RestaurantPhotoStrip(images = restaurant.images)
        Spacer(modifier = Modifier.height(SPACING_MD))
        Column(modifier = Modifier.padding(horizontal = SHEET_PADDING)) {
            RestaurantHeader(restaurant = restaurant)
            Spacer(modifier = Modifier.height(SPACING_SM))
            DetailRow(label = s.mapAddress, value = restaurant.address)
            DetailRow(label = s.mapHours, value = restaurant.hours)
            Spacer(modifier = Modifier.height(SPACING_MD))
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(SPACING_SM))
                Text(s.mapNavigate)
            }
        }
    }
}

@Composable
private fun AiPoweredBadge(modifier: Modifier = Modifier) {
    val s = LocalAppStrings.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(AI_BADGE_RADIUS),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = AI_BADGE_ELEVATION,
        tonalElevation = AI_BADGE_ELEVATION,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AI_BADGE_H_PADDING, vertical = AI_BADGE_V_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AI_BADGE_ICON_SPACING),
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(AI_BADGE_ICON_SIZE),
            )
            Text(
                text = s.mapAiPowered,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun RestaurantPhotoStrip(images: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SHEET_PADDING),
        horizontalArrangement = Arrangement.spacedBy(PHOTO_SPACING),
    ) {
        images.take(PHOTO_COUNT).forEach { url ->
            SubcomposeAsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = { PhotoPlaceholder() },
                error = { PhotoPlaceholder() },
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(PHOTO_CORNER_RADIUS)),
            )
        }
    }
}

@Composable
private fun PhotoPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}

@Composable
private fun RestaurantHeader(restaurant: ShopLocation) {
    val s = LocalAppStrings.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = restaurant.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(SPACING_XS))
            Text(
                text = localizedCuisine(restaurant.category, s),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.width(SPACING_SM))
        StarRating(rating = restaurant.rating)
    }
}

@Composable
private fun StarRating(rating: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = STAR_COLOR,
            modifier = Modifier.size(STAR_SIZE),
        )
        Spacer(modifier = Modifier.width(SPACING_XS))
        Text(
            text = roundToOneDecimal(rating),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SPACING_XS),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

private fun roundToOneDecimal(value: Float): String {
    val rounded = kotlin.math.round(value * 10) / 10.0
    return rounded.toString()
}

private fun localizedCuisine(raw: String, s: AppStrings): String = when (raw) {
    "Italian" -> s.cuisineItalian
    "Japanese" -> s.cuisineJapanese
    "Spanish Grill" -> s.cuisineSpanishGrill
    "French Café" -> s.cuisineFrenchCafe
    "Thai" -> s.cuisineThai
    "American" -> s.cuisineAmerican
    "Spanish Seafood" -> s.cuisineSpanishSeafood
    "Italian Pasta" -> s.cuisineItalianPasta
    "Healthy Bowls" -> s.cuisineHealthyBowls
    "Desserts" -> s.cuisineDesserts
    else -> raw
}

private val AI_BADGE_TOP_PADDING = 10.dp
private val AI_BADGE_H_PADDING = 12.dp
private val AI_BADGE_V_PADDING = 6.dp
private val AI_BADGE_ICON_SIZE = 14.dp
private val AI_BADGE_ICON_SPACING = 4.dp
private val AI_BADGE_RADIUS = 50.dp
private val AI_BADGE_ELEVATION = 4.dp
private val SHEET_PADDING = 20.dp
private val SHEET_BOTTOM_PADDING = 24.dp
private val SPACING_XS = 4.dp
private val SPACING_SM = 8.dp
private val SPACING_MD = 16.dp
private val PHOTO_SPACING = 8.dp
private val PHOTO_CORNER_RADIUS = 12.dp
private val STAR_SIZE = 18.dp
private val STAR_COLOR = Color(0xFFFFC107)
private const val PHOTO_COUNT = 3
