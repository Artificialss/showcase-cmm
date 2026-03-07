package com.artificialss.showcase.ui.components.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformMapView(
    markers: List<MapMarker>,
    cameraState: CameraState,
    onCameraMove: (CameraState) -> Unit,
    onMarkerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
)
