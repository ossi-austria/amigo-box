package org.ossiaustria.amigobox.ui

import androidx.compose.ui.unit.dp

class UIConstants {

    object Defaults {
        val OUTER_PADDING = 40.dp
        val INNER_PADDING = 8.dp
        val BOX_WIDTH = 450.dp
    }

    object ListCard {

        val AVATAR_IMAGE_HEIGHT = 200.dp
        val IMAGE_HEIGHT = 168.dp
        val CARD_WIDTH = 200.dp
        val TEXT_PADDING = 4.dp
        val RECT_HEIGHT = 4.dp
        val RECT_WIDTH = 50.dp
    }

    object ListFragment {
        val HEADER_PADDING_START = 40.dp
        val HEADER_PADDING_TOP = 4.dp
        val HEADER_PADDING_BOTTOM = 4.dp
        val HEADER_HEIGHT = 64.dp
        val DESCRIPTION_PADDING_START = 40.dp
        val DESCRIPTION_PADDING_TOP = 4.dp
        val DESCRIPTION_HEIGHT = 40.dp
    }

    object ScrollableCardList {
        val PADDING_START = 40.dp
        val PADDING_TOP = 8.dp
        val CARD_PADDING = 8.dp
        val CARD_ELEVATION = 0.dp
    }

    object HomeButtonRow {
        val END_PADDING = 40.dp
        val TOP_PADDING = 16.dp
        val HEIGHT = 88.dp
    }

    object NavigationButtonRow {
        val PADDING_START = 40.dp
        val PADDING_END = 40.dp
        val PADDING_BOTTOM = 16.dp
        val CONTACTS_PADDING_TOP = 32.dp
        val PADDING_TOP = 16.dp
        val HEIGHT = 40.dp
    }

    object ScrollButton {
        val SCROLL_DISTANCE = 800
    }

    object Slideshow {
        var DELAY: Long = 3000
    }

    object HomeButtonsCard {
        val CARD_HEIGHT = 250.dp
        val CARD_WIDTH = 250.dp
        val IMAGE_HEIGHT = 150.dp
        val IMAGE_WIDTH = 150.dp
        val TEXT_PADDING = 4.dp
        val TEXT_HEIGHT = 80.dp
        val CARD_PADDING = 16.dp
        val NOTIFICATION_PADDING_TOP = 16.dp
        val NOTIFICATION_PADDING_END = 8.dp
        val NOTIFICATION_SIZE = 32.dp
        val CARD_SHAPE = 24.dp
    }

    object HomeFragment {
        val PADDING_START = 40.dp
        val DESCRIPTION_PADDING_TOP = 8.dp
        val DESCRIPTION_PADDING_BOTTOM = 24.dp
        val HEADER_PADDING_TOP = 40.dp
    }

    object BigButtons {
        val ICON_SIZE = 40.dp
        val ICON_PADDING = 4.dp
        val BUTTON_WIDTH = 240.dp
        val BUTTON_HEIGHT = 64.dp
        val ROUNDED_CORNER = 40.dp
        val CARD_PADDING = 4.dp

    }

    object SmallButtons {
        val BUTTON_SIZE = 72.dp
        val ICON_SIZE = 32.dp
        val CARD_PADDING = 8.dp
    }

    object ProfileImage {
        val IMAGE_SIZE = 280.dp
        val IMAGE_PADDING = 24.dp
        val BORDER_WIDTH = 2.dp
    }

    object PersonDetailFragment {
        val SEC_ROW_PADDING = 16.dp
        val COLUMN_PADDING = 80.dp
        val CALL_BUTTON_WIDTH = 320.dp
        val ALBUM_BUTTON_WIDTH = 440.dp
    }

    object ScrollNavigationButton {
        val CARD_HEIGHT = 40.dp
        val CARD_WIDTH = 160.dp
        val ROW_HEIGHT = 40.dp
        val IMAGE_SIZE = 32.dp
        val IMAGE_PADDING = 4.dp
        val BUTTON_WIDTH = 220.dp
        val PLAY_IMAGE_SIZE = 24.dp
    }


    object TimelineFragment {
        val INDEX_BOX_HEIGHT = 32.dp
        val INDEX_BOX_WIDTH = 96.dp
        val HOME_HELP_BTN_COLUMN_WIDTH = 480.dp
        val SENDABLE_CARD_SPACER_PADDING = 150.dp
        val BUTTON_WIDTH = 200.dp
        val IMAGE_SIZE = 184.dp
        val PROFIL_IMAGE_COLUMN_PADDING_START = 64.dp
        val PROFIL_IMAGE_COLUMN_PADDING_END = 8.dp
        val CONTENT_TEXT_PADDING_TOP = 40.dp
        val CONTENT_TEXT_PADDING_BOTTOM = 8.dp
        val BOTTOM_PADDING = 16.dp

    }
    object CallFragmentConstants {
        val MIDDLE_ROW_HEIGHT = 350.dp
        val BOTTOM_ROW_HEIGHT = 200.dp // TODO static Size might be dangerous for that Row
        val COLUMN_WIDTH = 400.dp
        val COLUMN_START_PADDING = 160.dp
    }

}
