package org.ossiaustria.amigobox.ui.commons

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun textResString(@StringRes altTextRes: Int? = null, altText: String? = null): String =
    altTextRes?.let { stringResource(altTextRes) } ?: altText ?: ""

