package org.ossiaustria.amigobox.ui.commons

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.ossiaustria.amigobox.R

enum class NavigationButtonType {
    PREVIOUS, NEXT
}

@Composable
fun ScrollNavigationButton(
    type: NavigationButtonType,
    text: String,
    scrollState: ScrollState,
    onClick: () -> Unit,
) {

    when (type) {
        NavigationButtonType.PREVIOUS ->
            if (scrollState.value == 0) {
                Spacer(Modifier)
            } else {
                TextAndIconButton(
                    iconId = R.drawable.ic_arrow_left,
                    text = text,
                    onClick = onClick
                )
            }
        NavigationButtonType.NEXT ->
            if (scrollState.value == scrollState.maxValue) {
                Spacer(Modifier)
            } else {
                TextAndIconButton(
                    iconId = null,
                    endIconId = R.drawable.ic_arrow_right,
                    text = text,
                    onClick = onClick
                )
            }
    }
}


