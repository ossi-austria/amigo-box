package org.ossiaustria.amigobox.ui.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.ossiaustria.amigobox.ui.UIConstants

@Composable
fun HomeButtonRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .height(UIConstants.HomeButtonRow.HEIGHT)
            .padding(
                end = UIConstants.HomeButtonRow.TOP_PADDING,
                top = UIConstants.HomeButtonRow.TOP_PADDING
            ),
        content = content
    )
}