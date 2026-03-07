package com.artificialss.showcase.ui.components.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
actual fun PlatformMapView(
    markers: List<MapMarker>,
    cameraState: CameraState,
    onCameraMove: (CameraState) -> Unit,
    onMarkerClick: (String) -> Unit,
    modifier: Modifier,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(cameraState.latitude, cameraState.longitude),
            cameraState.zoom,
        )
    }

    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position }
            .debounce(CAMERA_DEBOUNCE_MS)
            .collect { position ->
                onCameraMove(
                    CameraState(
                        latitude = position.target.latitude,
                        longitude = position.target.longitude,
                        zoom = position.zoom,
                    ),
                )
            }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
    ) {
        markers.forEach { marker ->
            Marker(
                state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                title = marker.title,
                onClick = {
                    onMarkerClick(marker.id)
                    true
                },
            )
        }
    }
}

private const val CAMERA_DEBOUNCE_MS = 300L
