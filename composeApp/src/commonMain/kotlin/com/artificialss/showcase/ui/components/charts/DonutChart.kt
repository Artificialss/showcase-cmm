package com.artificialss.showcase.ui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class DonutChartSegment(
    val label: String,
    val value: Float,
    val color: Color,
)

@Composable
fun DonutChart(
    segments: List<DonutChartSegment>,
    centerLabel: String = "",
    centerValue: String = "",
    modifier: Modifier = Modifier,
) {
    if (segments.isEmpty()) return

    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(segments) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION_MS,
                easing = FastOutSlowInEasing,
            ),
        )
    }

    val total = segments.sumOf { it.value.toDouble() }.toFloat()

    var selectedIndex by remember { mutableIntStateOf(-1) }
    LaunchedEffect(segments) { selectedIndex = -1 }

    // Resolve selected segment info for center display
    val displayValue: String
    val displayLabel: String
    if (selectedIndex in segments.indices) {
        val seg = segments[selectedIndex]
        val pct = if (total > 0f) (seg.value / total * PERCENT_MULTIPLIER).roundToInt() else 0
        displayValue = formatChartValue(seg.value)
        displayLabel = "${seg.label} ($pct%)"
    } else {
        displayValue = centerValue
        displayLabel = centerLabel
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(DONUT_SIZE),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier
                    .size(DONUT_SIZE)
                    .pointerInput(segments) {
                        detectTapGestures { offset ->
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val dx = offset.x - cx
                            val dy = offset.y - cy
                            val dist = sqrt(dx * dx + dy * dy)

                            val strokePx = STROKE_WIDTH_PX
                            val minDim = minOf(size.width, size.height).toFloat()
                            val radius = (minDim - strokePx) / 2f
                            val innerR = radius - strokePx / 2f
                            val outerR = radius + strokePx / 2f

                            if (dist < innerR || dist > outerR + HIT_TOLERANCE) {
                                selectedIndex = -1
                                return@detectTapGestures
                            }

                            // Calculate angle (0° = top, clockwise)
                            var angleDeg = atan2(dy.toDouble(), dx.toDouble()) * 180.0 / PI
                            angleDeg = (angleDeg + 90.0 + 360.0) % 360.0

                            val totalFloat = segments.sumOf { it.value.toDouble() }.toFloat()
                            var cumulative = 0f
                            var hitIndex = -1
                            for (i in segments.indices) {
                                val segSweep = if (totalFloat > 0f) (segments[i].value / totalFloat) * FULL_CIRCLE_DEGREES else 0f
                                if (angleDeg >= cumulative && angleDeg < cumulative + segSweep) {
                                    hitIndex = i
                                    break
                                }
                                cumulative += segSweep + GAP_DEGREES
                            }

                            selectedIndex = if (hitIndex == selectedIndex) -1 else hitIndex
                        }
                    },
            ) {
                val strokeWidth = STROKE_WIDTH_PX
                val selectedStrokeWidth = SELECTED_STROKE_WIDTH_PX
                val radius = (size.minDimension - strokeWidth) / 2f
                val topLeft = androidx.compose.ui.geometry.Offset(
                    (size.width - radius * 2) / 2f,
                    (size.height - radius * 2) / 2f,
                )
                val arcSize = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)

                // Track
                drawArc(
                    color = Color.LightGray.copy(alpha = TRACK_ALPHA),
                    startAngle = 0f,
                    sweepAngle = FULL_CIRCLE_DEGREES,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )

                // Pre-compute angles and sweeps (subtract gap space from available degrees)
                val totalGap = GAP_DEGREES * segments.size
                val availableDegrees = FULL_CIRCLE_DEGREES - totalGap
                val sweeps = segments.map { segment ->
                    if (total > 0f) (segment.value / total) * availableDegrees * animationProgress.value else 0f
                }
                val startAngles = FloatArray(segments.size)
                var angle = START_ANGLE
                for (i in segments.indices) {
                    startAngles[i] = angle
                    angle += sweeps[i] + GAP_DEGREES
                }

                // Draw non-selected segments first
                segments.forEachIndexed { index, segment ->
                    if (index != selectedIndex) {
                        drawArc(
                            color = segment.color,
                            startAngle = startAngles[index],
                            sweepAngle = sweeps[index],
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                        )
                    }
                }

                // Draw selected segment last (on top) — larger radius, no origin offset
                if (selectedIndex in segments.indices) {
                    val segment = segments[selectedIndex]
                    val sweep = sweeps[selectedIndex]
                    val startAng = startAngles[selectedIndex]
                    val grow = (selectedStrokeWidth - strokeWidth) / 2f
                    val selectedRadius = radius + grow
                    val selectedTopLeft = androidx.compose.ui.geometry.Offset(
                        (size.width - selectedRadius * 2) / 2f,
                        (size.height - selectedRadius * 2) / 2f,
                    )
                    val selectedArcSize = androidx.compose.ui.geometry.Size(
                        selectedRadius * 2,
                        selectedRadius * 2,
                    )
                    drawArc(
                        color = segment.color,
                        startAngle = startAng,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = selectedTopLeft,
                        size = selectedArcSize,
                        style = Stroke(width = selectedStrokeWidth, cap = StrokeCap.Butt),
                    )
                }
            }

            // Center text
            if (displayLabel.isNotBlank() || displayValue.isNotBlank()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (displayValue.isNotBlank()) {
                        Text(
                            text = displayValue,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if (displayLabel.isNotBlank()) {
                        Text(
                            text = displayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(LEGEND_SPACING))

        Column(
            modifier = Modifier.height(DONUT_SIZE),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            segments.forEachIndexed { index, segment ->
                LegendItem(
                    segment = segment,
                    total = total,
                    isSelected = index == selectedIndex,
                )
            }
        }
    }
}

@Composable
private fun LegendItem(
    segment: DonutChartSegment,
    total: Float,
    isSelected: Boolean,
) {
    val percentage = if (total > 0f) (segment.value / total * PERCENT_MULTIPLIER).toInt() else 0

    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(LEGEND_DOT_SIZE)) {
            drawCircle(color = segment.color)
        }
        Spacer(modifier = Modifier.width(LEGEND_DOT_SPACING))
        Text(
            text = segment.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(LEGEND_DOT_SPACING))
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private val DONUT_SIZE = 140.dp
private val LEGEND_SPACING = 16.dp
private val LEGEND_DOT_SIZE = 10.dp
private val LEGEND_DOT_SPACING = 6.dp
private const val STROKE_WIDTH_PX = 24f
private const val SELECTED_STROKE_WIDTH_PX = 30f
private const val START_ANGLE = -90f
private const val FULL_CIRCLE_DEGREES = 360f
private const val GAP_DEGREES = 2f
private const val TRACK_ALPHA = 0.2f
private const val PERCENT_MULTIPLIER = 100f
private const val ANIMATION_DURATION_MS = 800
private const val HIT_TOLERANCE = 20f
