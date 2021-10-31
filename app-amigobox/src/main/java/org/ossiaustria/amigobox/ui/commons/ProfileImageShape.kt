package org.ossiaustria.amigobox.ui.commons

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

/**
 * This class defines the shape of profile image, but currently it isn't working properly.
 * It can be changed and used in the future for better design.
 * In drawShapePath instead of using addOval, addArc or toArc should be used.
 * Currently the image in this shape has cut in place where triangle and circle collide.*/

class ProfileImageShape(private val radius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawShapePath(size = size, cornerRadius = radius)
        )
    }

    private fun drawShapePath(size: Size, cornerRadius: Float): Path {
        return Path().apply {
            reset()
            moveTo(x = size.width, y = size.height / 2)
            lineTo(x = size.width, y = size.height)
            lineTo(x = size.width / 2, y = size.height)
            moveTo(x = 0f, y = 0f)
            addOval(
                Rect(
                    left = cornerRadius,
                    top = cornerRadius,
                    right = 10f,
                    bottom = 10f
                ),
            )

            close()
        }
    }
}


