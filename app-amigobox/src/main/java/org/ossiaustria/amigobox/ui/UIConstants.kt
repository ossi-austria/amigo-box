package org.ossiaustria.amigobox.ui

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class UIConstants {
    object ListCard {

        // Person Card
        val AVATAR_IMAGE_HEIGHT = 200.dp
        val CARD_WIDTH = 200.dp
        val TEXT_PADDING = 4.dp
    }

    object ListCardContact {
        val NAME_FONT_SIZE = 32.sp
        val TEXT_HEIGHT = 40.dp
    }

    object ListCardAlbum {
        val NAME_FONT_SIZE = 22.sp
        val TEXT_HEIGHT = 60.dp
    }

    object ListFragment {
        val HEADER_PADDING_START = 40.dp
        val HEADER_PADDING_TOP = 4.dp
        val HEADER_PADDING_BOTTOM = 4.dp
        val HEADER_HEIGHT = 50.dp
        val HEADER_FONT_SIZE = 40.sp

        val DESCRIPTION_PADDING_START = 40.dp
        val DESCRIPTION_PADDING_TOP = 4.dp
        val DESCRIPTION_HEIGHT = 40.dp
        val DESCRIPTION_FONT_SIZE = 16.sp
    }

    object ScrollableCardList {
        val PADDING_START = 0.dp
        val PADDING_TOP = 16.dp
        val CARD_PADDING = 8.dp
        val CARD_ELEVATION = 0.dp
    }

    object HomeButtonRow {
        val END_PADDING = 40.dp
        val TOP_PADDING = 16.dp
        val HEIGHT = 40.dp
    }

    object NavigationButtonRow {
        val PADDING_START = 40.dp
        val PADDING_END = 40.dp
        val PADDING_BOTTOM = 16.dp
        val HEIGHT = 80.dp
    }

    object ScrollButton {
        val SCROLL_DISTANCE = 800
    }

    object Slideshow {
        var DELAY: Long = 3000
    }

}