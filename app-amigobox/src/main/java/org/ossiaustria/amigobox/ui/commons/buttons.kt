package org.ossiaustria.amigobox.ui.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


// reuse a Composable - there are no styles
@Composable
fun MaterialButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier? = null,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = Color.Blue
    )
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier ?: Modifier.fillMaxWidth(),
        colors = colors
    ) {
        Text(text = text)
    }
}