package org.ossiaustria.amigobox.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.IconButtonSmall

/**
 * TODO: How is this HelpColumn different of others ?
 * Think how this can be refactored and reused, e.g. to a "HelpButtonColumn" which can be used *generally*
 */
@Composable
fun HomeHelpButtonColumn(toHome: () -> Unit) {
    Column(
        // TODO: should not be constant width! Just focus on "X Dp from the right"
        modifier = Modifier.width(UIConstants.TimelineFragment.HOME_HELP_BTN_COLUMN_WIDTH),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButtonSmall(
                resourceId = R.drawable.ic_home_icon,
                backgroundColor = MaterialTheme.colors.secondary,
                fillColor = MaterialTheme.colors.surface,
            ) {
                toHome()
            }
            IconButtonSmall(
                resourceId = R.drawable.ic_help_icon,
                backgroundColor = MaterialTheme.colors.secondary,
                fillColor = MaterialTheme.colors.primary,
            ) {
                //TODO: Add help screens
            }
        }
    }
}
