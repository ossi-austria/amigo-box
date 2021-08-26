package org.ossiaustria.amigobox.ui.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ossiaustria.amigobox.ui.commons.NetworkImage
import org.ossiaustria.amigobox.ui.contacts.PersonImage
import org.ossiaustria.lib.domain.models.Album

@Composable
fun LoadAlbumCardContent(album: Album, previewImageUrl: String) {
    // TODO: change descrpition

    Row(
        modifier = Modifier
            .height(200.dp)
            .width(200.dp),
    ) {
        NetworkImage(
            modifier = Modifier.fillMaxSize(),
            url = previewImageUrl,
            contentScale = ContentScale.Crop,

        )
    }
    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(60.dp)
            .width(200.dp),
    ){
        Text(
            text = album.name,
            style = MaterialTheme.typography.h2,
            fontSize = 26.sp,
            maxLines = 2
        )
    }
}





