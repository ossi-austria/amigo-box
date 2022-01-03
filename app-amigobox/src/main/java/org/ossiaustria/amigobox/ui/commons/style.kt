package org.ossiaustria.amigobox.ui.commons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

object AmigoStyle {

    object Images {
        @Composable
        fun roundBorders() = Modifier.clip(RoundedCornerShape(size = 16.dp))
    }
}