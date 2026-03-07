package com.artificialss.showcase.ui.feature.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

        if (state.selectedShop != null) {
            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = onSheetDismissed,
                sheetState = sheetState,
            ) {
                ShopDetailSheet(shop = state.selectedShop)
            }
        }
    }
}

@Composable
private fun ShopDetailSheet(shop: ShopLocation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SHEET_PADDING),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(ICON_SPACING))
            Text(
                text = shop.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(SPACING_SM))
        DetailRow(label = "Category", value = shop.category)
        DetailRow(label = "Address", value = shop.address)
        DetailRow(label = "Rating", value = "${roundToOneDecimal(shop.rating)} / 5.0")
        DetailRow(label = "Hours", value = shop.hours)
        Spacer(modifier = Modifier.height(SECTION_SPACING))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null)
            Spacer(modifier = Modifier.width(SPACING_SM))
            Text("Navigate")
        }
        Spacer(modifier = Modifier.height(SECTION_SPACING))
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

private val SECTION_SPACING = 16.dp
private val SHEET_PADDING = 24.dp
private val ICON_SPACING = 12.dp
private val SPACING_XS = 4.dp
private val SPACING_SM = 8.dp
