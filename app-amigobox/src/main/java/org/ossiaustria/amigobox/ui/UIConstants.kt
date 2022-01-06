package org.ossiaustria.amigobox.ui

import androidx.compose.ui.unit.dp
import org.ossiaustria.amigobox.ui.UIConstants.Defaults.OUTER_PADDING

class UIConstants {

    object Defaults {
        val OUTER_PADDING = 40.dp
        val INNER_PADDING = 8.dp
        val CARD_WIDTH = 200.dp
        val TEXT_PADDING = 4.dp
        val CARD_ELEVATION = 0.dp
    }

    object ListCard {
        val AVATAR_IMAGE_HEIGHT = 200.dp
        val IMAGE_HEIGHT = 168.dp
        val RECT_HEIGHT = 4.dp
    }

    object NavigationButtonRow {
        val PADDING_START = OUTER_PADDING
        val PADDING_END = OUTER_PADDING
        val PADDING_BOTTOM = 24.dp
        val PADDING_TOP = 16.dp
    }

    object ScrollButton {
        val SCROLL_DISTANCE = 800
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
        val DESCRIPTION_PADDING_TOP = 4.dp
        val DESCRIPTION_PADDING_BOTTOM = 16.dp
        val HEADER_PADDING_TOP = 40.dp
    }

    object BigButtons {
        val ICON_SIZE = 40.dp
        val ICON_PADDING = 8.dp
        val BUTTON_HEIGHT = 64.dp
        val ROUNDED_CORNER = BUTTON_HEIGHT / 2
    }

    object ProfileImage {
        val IMAGE_SIZE = 280.dp
        val IMAGE_PADDING = 24.dp
        val BORDER_WIDTH = 2.dp
    }

    object PersonDetailFragment {
        val SEC_ROW_PADDING = 16.dp
        val SEC_ROW_PADDING_START = 40.dp
        val COLUMN_PADDING = 80.dp
    }

    object TimelineFragment {
        val PADDING = 16.dp
        val BIGGER_PADDING = 48.dp
    }
}
