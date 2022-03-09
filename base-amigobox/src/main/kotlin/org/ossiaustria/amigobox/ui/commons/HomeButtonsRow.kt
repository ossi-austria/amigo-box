package org.ossiaustria.amigobox.ui.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants

@Composable
fun HomeButtonsRow(
    onClickBack: () -> Unit,
    onClickHelp: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(UIConstants.Defaults.INNER_PADDING),
        horizontalArrangement = Arrangement.End
    ) {
        Spacer(Modifier.weight(1F))
        // Home Button
        TextAndIconButton(
            iconId = R.drawable.ic_home_icon,
            text = stringResource(id = R.string.back_home_description),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            onClick = onClickBack
        )
        DefaultHelpButton(onClickHelp)
    }
}

@Composable
fun DefaultHelpButton(
    onClickHelp: () -> Unit = {},
) {
    TextAndIconButton(
        iconId = R.drawable.ic_help_icon,
        text = stringResource(R.string.help_button_description),
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onPrimary,
        onClick = onClickHelp
    )
}