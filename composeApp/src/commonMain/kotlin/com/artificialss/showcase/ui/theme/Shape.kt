package com.artificialss.showcase.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

fun shapesFor(style: ShapeStyle): Shapes = when (style) {
    ShapeStyle.ROUNDED -> Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
        extraLarge = RoundedCornerShape(24.dp),
    )
    ShapeStyle.SQUARE -> Shapes(
        extraSmall = RoundedCornerShape(0.dp),
        small = RoundedCornerShape(0.dp),
        medium = RoundedCornerShape(0.dp),
        large = RoundedCornerShape(0.dp),
        extraLarge = RoundedCornerShape(0.dp),
    )
    ShapeStyle.PILL -> Shapes(
        extraSmall = RoundedCornerShape(percent = 50),
        small = RoundedCornerShape(percent = 50),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(12.dp),
        extraLarge = RoundedCornerShape(12.dp),
    )
}
