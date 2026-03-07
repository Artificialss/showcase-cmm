package com.artificialss.showcase.ui.components.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
actual fun PlatformMapView(
    markers: List<MapMarker>,
    cameraState: CameraState,
    onCameraMove: (CameraState) -> Unit,
    onMarkerClick: (String) -> Unit,
    modifier: Modifier,
) {
    var localCamera by remember(cameraState) { mutableStateOf(cameraState) }
    val textMeasurer = rememberTextMeasurer()
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val outline = MaterialTheme.colorScheme.outline.copy(alpha = GRID_ALPHA)
    val labelStyle = TextStyle(fontSize = LABEL_FONT_SIZE.sp, color = MaterialTheme.colorScheme.onSurface)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceVariant)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = { onCameraMove(localCamera) },
                ) { change, dragAmount ->
                    change.consume()
                    val scale = DRAG_SCALE / zoomScale(localCamera.zoom)
                    localCamera = localCamera.copy(
                        latitude = localCamera.latitude - dragAmount.y * scale,
                        longitude = localCamera.longitude + dragAmount.x * scale,
                    )
                }
            }
            .pointerInput(markers) {
                detectTapGestures { tapOffset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()
                    val scale = zoomScale(localCamera.zoom)

                    markers.forEach { marker ->
                        val mx = w / 2f + ((marker.longitude - localCamera.longitude) * scale * LNG_TO_PX).toFloat()
                        val my = h / 2f - ((marker.latitude - localCamera.latitude) * scale * LAT_TO_PX).toFloat()
                        val dist = sqrt(
                            (tapOffset.x - mx) * (tapOffset.x - mx) +
                                (tapOffset.y - my) * (tapOffset.y - my),
                        )
                        if (dist < TAP_RADIUS) {
                            onMarkerClick(marker.id)
                            return@detectTapGestures
                        }
                    }
                }
            },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawGrid(outline, localCamera)

            val scale = zoomScale(localCamera.zoom)
            markers.forEach { marker ->
                val x = size.width / 2f + ((marker.longitude - localCamera.longitude) * scale * LNG_TO_PX).toFloat()
                val y = size.height / 2f - ((marker.latitude - localCamera.latitude) * scale * LAT_TO_PX).toFloat()

                if (x in -MARKER_SIZE..size.width + MARKER_SIZE && y in -MARKER_SIZE..size.height + MARKER_SIZE) {
                    drawMarkerPin(Offset(x, y), primary, onPrimary)
                    drawText(
                        textMeasurer = textMeasurer,
                        text = marker.title,
                        topLeft = Offset(x - LABEL_OFFSET_X, y - LABEL_OFFSET_Y),
                        style = labelStyle,
                    )
                }
            }
        }
    }
}

private fun zoomScale(zoom: Float): Float = 2.0.pow((zoom - BASE_ZOOM).toDouble()).toFloat()

private fun DrawScope.drawGrid(color: Color, camera: CameraState) {
    val scale = zoomScale(camera.zoom)
    val gridSpacing = GRID_BASE_SPACING * scale

    val offsetX = ((camera.longitude * scale * LNG_TO_PX) % gridSpacing).toFloat()
    val offsetY = ((camera.latitude * scale * LAT_TO_PX) % gridSpacing).toFloat()

    var x = -gridSpacing + (size.width / 2f % gridSpacing) - offsetX
    while (x < size.width + gridSpacing) {
        drawLine(color, Offset(x, 0f), Offset(x, size.height), strokeWidth = GRID_STROKE)
        x += gridSpacing
    }

    var y = -gridSpacing + (size.height / 2f % gridSpacing) + offsetY
    while (y < size.height + gridSpacing) {
        drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = GRID_STROKE)
        y += gridSpacing
    }
}

private fun DrawScope.drawMarkerPin(center: Offset, color: Color, innerColor: Color) {
    val pinPath = Path().apply {
        moveTo(center.x, center.y)
        lineTo(center.x - PIN_HALF_WIDTH, center.y - PIN_BODY_HEIGHT)
        quadraticTo(
            center.x - PIN_HALF_WIDTH, center.y - PIN_TOTAL_HEIGHT,
            center.x, center.y - PIN_TOTAL_HEIGHT,
        )
        quadraticTo(
            center.x + PIN_HALF_WIDTH, center.y - PIN_TOTAL_HEIGHT,
            center.x + PIN_HALF_WIDTH, center.y - PIN_BODY_HEIGHT,
        )
        close()
    }
    drawPath(pinPath, color, style = Fill)
    drawPath(pinPath, color.copy(alpha = 0.6f), style = Stroke(width = 2f))
    drawCircle(innerColor, radius = PIN_INNER_RADIUS, center = Offset(center.x, center.y - PIN_CENTER_Y))
}

private const val GRID_ALPHA = 0.15f
private const val GRID_STROKE = 1f
private const val GRID_BASE_SPACING = 60f
private const val BASE_ZOOM = 14f
private const val DRAG_SCALE = 0.00002
private const val LNG_TO_PX = 80000.0
private const val LAT_TO_PX = 110000.0
private const val TAP_RADIUS = 40f
private const val MARKER_SIZE = 40f
private const val PIN_HALF_WIDTH = 12f
private const val PIN_BODY_HEIGHT = 20f
private const val PIN_TOTAL_HEIGHT = 36f
private const val PIN_INNER_RADIUS = 5f
private const val PIN_CENTER_Y = 26f
private const val LABEL_OFFSET_X = 30f
private const val LABEL_OFFSET_Y = 50f
private val LABEL_FONT_SIZE = 10
