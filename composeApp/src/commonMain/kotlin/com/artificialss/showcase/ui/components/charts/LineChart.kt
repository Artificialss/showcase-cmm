package com.artificialss.showcase.ui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

data class LineChartEntry(
    val label: String,
    val value: Float,
)

@Composable
fun LineChart(
    entries: List<LineChartEntry>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = FILL_ALPHA),
) {
    if (entries.size < MIN_ENTRIES) return

    val textMeasurer = rememberTextMeasurer()
    val tooltipBg = MaterialTheme.colorScheme.inverseSurface
    val tooltipTextColor = MaterialTheme.colorScheme.inverseOnSurface
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant
    val indicatorColor = lineColor.copy(alpha = INDICATOR_LINE_ALPHA)

    var selectedIndex by remember { mutableIntStateOf(-1) }

    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(entries) {
        selectedIndex = -1
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION_MS,
                easing = FastOutSlowInEasing,
            ),
        )
    }

    val maxValue = entries.maxOf { it.value }
    val minValue = entries.minOf { it.value }
    val valueRange = (maxValue - minValue).coerceAtLeast(1f)

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(CHART_HEIGHT)
                .pointerInput(entries) {
                    detectTapGestures { offset ->
                        val tooltipReserve = TOOLTIP_AREA.toPx()
                        val chartH = size.height - tooltipReserve
                        val stepX = size.width.toFloat() / (entries.size - 1)

                        val nearest = entries.indices.minByOrNull { i ->
                            abs(offset.x - i * stepX)
                        }
                        selectedIndex = if (nearest == selectedIndex) -1 else nearest ?: -1
                    }
                },
        ) {
            val tooltipReserve = TOOLTIP_AREA.toPx()
            val chartH = size.height - tooltipReserve
            val stepX = size.width / (entries.size - 1)

            val points = entries.mapIndexed { index, entry ->
                val x = index * stepX
                val normalizedY = (entry.value - minValue) / valueRange
                val y = tooltipReserve + chartH - (normalizedY * chartH * animationProgress.value)
                Offset(x, y)
            }

            // Fill gradient
            val fillPath = Path().apply {
                moveTo(points.first().x, size.height)
                points.forEach { lineTo(it.x, it.y) }
                lineTo(points.last().x, size.height)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(fillColor, fillColor.copy(alpha = 0f)),
                    startY = tooltipReserve,
                    endY = size.height,
                ),
            )

            // Cubic bezier line
            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val midX = (prev.x + curr.x) / 2f
                    cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
                }
            }
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = LINE_STROKE_WIDTH, cap = StrokeCap.Round, join = StrokeJoin.Round),
            )

            // Dots
            points.forEachIndexed { index, point ->
                val isSelected = index == selectedIndex
                drawCircle(
                    color = lineColor,
                    radius = if (isSelected) DOT_SELECTED_RADIUS else DOT_RADIUS,
                    center = point,
                )
                drawCircle(
                    color = Color.White,
                    radius = if (isSelected) DOT_SELECTED_INNER else DOT_INNER_RADIUS,
                    center = point,
                )
            }

            // Selected indicator line + tooltip
            if (selectedIndex in entries.indices) {
                val point = points[selectedIndex]

                drawLine(
                    color = indicatorColor,
                    start = Offset(point.x, tooltipReserve),
                    end = Offset(point.x, size.height),
                    strokeWidth = INDICATOR_LINE_WIDTH,
                )

                val entry = entries[selectedIndex]
                val text = "${entry.label}: ${formatChartValue(entry.value)}"
                val tooltipStyle = TextStyle(
                    color = tooltipTextColor,
                    fontSize = TOOLTIP_FONT_SIZE,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
                drawLineTooltip(
                    textMeasurer = textMeasurer,
                    text = text,
                    style = tooltipStyle,
                    anchorX = point.x,
                    anchorY = point.y,
                    bgColor = tooltipBg,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = LABEL_TOP_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            entries.forEach { entry ->
                Text(
                    text = entry.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = labelColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

private fun DrawScope.drawLineTooltip(
    textMeasurer: TextMeasurer,
    text: String,
    style: TextStyle,
    anchorX: Float,
    anchorY: Float,
    bgColor: Color,
) {
    val measured = textMeasurer.measure(text, style)
    val textW = measured.size.width.toFloat()
    val textH = measured.size.height.toFloat()
    val bubbleW = textW + TOOLTIP_PAD_H * 2
    val bubbleH = textH + TOOLTIP_PAD_V * 2

    val wouldClipTop = anchorY - bubbleH - TOOLTIP_TRIANGLE_H - TOOLTIP_GAP < 0f
    val bubbleTop: Float
    val triangleDown: Boolean

    if (wouldClipTop) {
        bubbleTop = anchorY + DOT_SELECTED_RADIUS + TOOLTIP_TRIANGLE_H + TOOLTIP_GAP
        triangleDown = false
    } else {
        bubbleTop = anchorY - DOT_SELECTED_RADIUS - bubbleH - TOOLTIP_TRIANGLE_H - TOOLTIP_GAP
        triangleDown = true
    }

    val bubbleLeft = (anchorX - bubbleW / 2).coerceIn(0f, size.width - bubbleW)

    drawRoundRect(
        color = bgColor,
        topLeft = Offset(bubbleLeft, bubbleTop),
        size = Size(bubbleW, bubbleH),
        cornerRadius = CornerRadius(TOOLTIP_CORNER),
    )

    val clampedTipX = anchorX.coerceIn(bubbleLeft + TOOLTIP_TRIANGLE_HALF, bubbleLeft + bubbleW - TOOLTIP_TRIANGLE_HALF)
    val tri = Path()
    if (triangleDown) {
        val triTop = bubbleTop + bubbleH
        tri.moveTo(clampedTipX - TOOLTIP_TRIANGLE_HALF, triTop)
        tri.lineTo(clampedTipX, triTop + TOOLTIP_TRIANGLE_H)
        tri.lineTo(clampedTipX + TOOLTIP_TRIANGLE_HALF, triTop)
        tri.close()
    } else {
        val triBottom = bubbleTop
        tri.moveTo(clampedTipX - TOOLTIP_TRIANGLE_HALF, triBottom)
        tri.lineTo(clampedTipX, triBottom - TOOLTIP_TRIANGLE_H)
        tri.lineTo(clampedTipX + TOOLTIP_TRIANGLE_HALF, triBottom)
        tri.close()
    }
    drawPath(tri, bgColor)

    drawText(
        measured,
        topLeft = Offset(bubbleLeft + TOOLTIP_PAD_H, bubbleTop + TOOLTIP_PAD_V),
    )
}

private val CHART_HEIGHT = 220.dp
private val TOOLTIP_AREA = 40.dp
private val LABEL_TOP_PADDING = 4.dp
private const val LINE_STROKE_WIDTH = 3f
private const val DOT_RADIUS = 5f
private const val DOT_INNER_RADIUS = 2.5f
private const val DOT_SELECTED_RADIUS = 8f
private const val DOT_SELECTED_INNER = 4f
private const val FILL_ALPHA = 0.15f
private const val INDICATOR_LINE_ALPHA = 0.3f
private const val INDICATOR_LINE_WIDTH = 1f
private const val ANIMATION_DURATION_MS = 800
private const val MIN_ENTRIES = 2

private val TOOLTIP_FONT_SIZE = 11.sp
private const val TOOLTIP_PAD_H = 10f
private const val TOOLTIP_PAD_V = 6f
private const val TOOLTIP_CORNER = 8f
private const val TOOLTIP_TRIANGLE_H = 6f
private const val TOOLTIP_TRIANGLE_HALF = 5f
private const val TOOLTIP_GAP = 4f
