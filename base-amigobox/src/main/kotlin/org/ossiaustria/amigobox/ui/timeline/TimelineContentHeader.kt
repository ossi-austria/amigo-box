package org.ossiaustria.amigobox.ui.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.TimeUtils
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import java.util.*

@Composable
fun TimelineHeaderTitle(
    sendable: Sendable,
    findPerson: (UUID) -> Person?,
    centerPerson: Person
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            stringResource(R.string.fullDateString, TimeUtils.fullDate(sendable.time)),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(UIConstants.Defaults.INNER_PADDING)
        )
        val otherPersonName =
            findPerson(sendable.otherPersonId(centerPerson.id))?.name ?: ""
        Text(
            text = stringForSendable(sendable, otherPersonName),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(UIConstants.Defaults.INNER_PADDING)
        )
    }
}

@Composable
fun TimelineHeaderCounter(
    currentIndex: Int,
    sendables: List<Sendable>
) {
    Box(
        Modifier
            .padding(8.dp)
            .padding(bottom = 18.dp)
            .background(MaterialTheme.colors.primary, shape = CircleShape)
            .padding(10.dp)
    ) {
        Text(
            "${currentIndex + 1} / ${sendables.size}",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body1
        )
    }
}