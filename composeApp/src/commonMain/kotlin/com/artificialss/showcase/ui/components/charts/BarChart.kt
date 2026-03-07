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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BarChartEntry(
    val label: String,
    val value: Float,
    val color: Color? = null,
)

@Composable
fun BarChart(
    entries: List<BarChartEntry>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
) {
    if (entries.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val tooltipBg = MaterialTheme.colorScheme.inverseSurface
    val tooltipTextColor = MaterialTheme.colorScheme.inverseOnSurface
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant

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

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(CHART_HEIGHT)
                .pointerInput(entries) {
                    detectTapGestures { offset ->
                        val barCount = entries.size
                        val totalSpacing = BAR_SPACING_PX * (barCount - 1)
                        val barWidth = (size.width - totalSpacing) / barCount
                        val tappedIndex = entries.indices.firstOrNull { i ->
                            val barLeft = i * (barWidth + BAR_SPACING_PX)
                            offset.x in barLeft..(barLeft + barWidth)
                        }
                        selectedIndex = if (tappedIndex == selectedIndex) -1
                        else tappedIndex ?: -1
                    }
                },
        ) {
            val tooltipReserve = TOOLTIP_AREA.toPx()
            val chartHeight = size.height - tooltipReserve
            val barCount = entries.size
            val totalSpacing = BAR_SPACING_PX * (barCount - 1)
            val barWidth = (size.width - totalSpacing) / barCount

            entries.forEachIndexed { index, entry ->
                val barH = if (maxValue > 0f) {
                    (entry.value / maxValue) * chartHeight * animationProgress.value
                } else {
                    0f
                }
                val xOffset = index * (barWidth + BAR_SPACING_PX)
                val yOffset = size.height - barH
                val color = entry.color ?: barColor
                val isSelected = index == selectedIndex

                if (isSelected) {
                    drawRoundRect(
                        color = color.copy(alpha = HIGHLIGHT_ALPHA),
                        topLeft = Offset(xOffset, tooltipReserve),
                        size = Size(barWidth, chartHeight),
                        cornerRadius = CornerRadius(BAR_CORNER_RADIUS_PX),
                    )
                }

                drawRoundRect(
                    color = color,
                    topLeft = Offset(xOffset, yOffset),
                    size = Size(barWidth, barH),
                    cornerRadius = CornerRadius(BAR_CORNER_RADIUS_PX),
                )
            }

            if (selectedIndex in entries.indices) {
                val entry = entries[selectedIndex]
                val barXCenter = selectedIndex * (barWidth + BAR_SPACING_PX) + barWidth / 2
                val barH = if (maxValue > 0f) (entry.value / maxValue) * chartHeight * animationProgress.value else 0f
                val barTop = size.height - barH

                val text = "${entry.label}: ${formatChartValue(entry.value)}"
                val tooltipStyle = TextStyle(
                    color = tooltipTextColor,
                    fontSize = TOOLTIP_FONT_SIZE,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
                val measured = textMeasurer.measure(text, tooltipStyle)
                drawTooltipBubble(
                    textMeasurer = textMeasurer,
                    text = text,
                    style = tooltipStyle,
                    anchorX = barXCenter,
                    anchorY = barTop,
                    bgColor = tooltipBg,
                    canvasHeight = size.height,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = LABEL_TOP_PADDING),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            entries.forEach { entry ->
                Text(
                    text = entry.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = labelColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private fun DrawScope.drawTooltipBubble(
    textMeasurer: TextMeasurer,
    text: String,
    style: TextStyle,
    anchorX: Float,
    anchorY: Float,
    bgColor: Color,
    canvasHeight: Float,
) {
    val measured = textMeasurer.measure(text, style)
    val textWidth = measured.size.width.toFloat()
    val textHeight = measured.size.height.toFloat()
    val bubbleW = textWidth + TOOLTIP_PAD_H * 2
    val bubbleH = textHeight + TOOLTIP_PAD_V * 2

    val wouldClipTop = anchorY - bubbleH - TOOLTIP_TRIANGLE_H - TOOLTIP_GAP < 0f
    val bubbleTop: Float
    val trianglePointsDown: Boolean

    if (wouldClipTop) {
        bubbleTop = anchorY + TOOLTIP_TRIANGLE_H + TOOLTIP_GAP
        trianglePointsDown = false
    } else {
        bubbleTop = anchorY - bubbleH - TOOLTIP_TRIANGLE_H - TOOLTIP_GAP
        trianglePointsDown = true
    }

    val bubbleLeft = (anchorX - bubbleW / 2).coerceIn(0f, size.width - bubbleW)

    drawRoundRect(
        color = bgColor,
        topLeft = Offset(bubbleLeft, bubbleTop),
        size = Size(bubbleW, bubbleH),
        cornerRadius = CornerRadius(TOOLTIP_CORNER),
    )

    val clampedTipX = anchorX.coerceIn(bubbleLeft + TOOLTIP_TRIANGLE_HALF, bubbleLeft + bubbleW - TOOLTIP_TRIANGLE_HALF)
    val trianglePath = Path()
    if (trianglePointsDown) {
        val triTop = bubbleTop + bubbleH
        trianglePath.moveTo(clampedTipX - TOOLTIP_TRIANGLE_HALF, triTop)
        trianglePath.lineTo(clampedTipX, triTop + TOOLTIP_TRIANGLE_H)
        trianglePath.lineTo(clampedTipX + TOOLTIP_TRIANGLE_HALF, triTop)
        trianglePath.close()
    } else {
        val triBottom = bubbleTop
        trianglePath.moveTo(clampedTipX - TOOLTIP_TRIANGLE_HALF, triBottom)
        trianglePath.lineTo(clampedTipX, triBottom - TOOLTIP_TRIANGLE_H)
        trianglePath.lineTo(clampedTipX + TOOLTIP_TRIANGLE_HALF, triBottom)
        trianglePath.close()
    }
    drawPath(trianglePath, bgColor)

    drawText(
        measured,
        topLeft = Offset(bubbleLeft + TOOLTIP_PAD_H, bubbleTop + TOOLTIP_PAD_V),
    )
}

internal fun formatChartValue(value: Float): String {
    val longVal = value.toLong()
    if (value == longVal.toFloat()) return longVal.toString()
    return "${kotlin.math.round(value * 10f).toLong() / 10.0}"
}

private val CHART_HEIGHT = 200.dp
private val TOOLTIP_AREA = 40.dp
private val LABEL_TOP_PADDING = 4.dp
private const val BAR_SPACING_PX = 12f
private const val BAR_CORNER_RADIUS_PX = 8f
private const val ANIMATION_DURATION_MS = 700
private const val HIGHLIGHT_ALPHA = 0.1f

private val TOOLTIP_FONT_SIZE = 11.sp
private const val TOOLTIP_PAD_H = 10f
private const val TOOLTIP_PAD_V = 6f
private const val TOOLTIP_CORNER = 8f
private const val TOOLTIP_TRIANGLE_H = 6f
private const val TOOLTIP_TRIANGLE_HALF = 5f
private const val TOOLTIP_GAP = 4f
