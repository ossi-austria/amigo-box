package org.ossiaustria.amigobox.ui.commons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

object AmigoStyle {

    object Dim {
        val A = 2.dp
        val B = 4.dp
        val C = 8.dp
        val D = 16.dp
    }

    object Images {

        @Composable
        fun roundBorders() = Modifier.clip(RoundedCornerShape(size = Dim.D))
    }
}