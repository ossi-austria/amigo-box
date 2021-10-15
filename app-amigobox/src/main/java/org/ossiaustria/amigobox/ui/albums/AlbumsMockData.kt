package org.ossiaustria.amigobox.ui.albums

import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.math.BigInteger
import java.util.*
import java.util.UUID.randomUUID

val mockUUID1 = randomUUID()
val mockUUID2 = randomUUID()
val mockUUID3 = randomUUID()

val multimediaList = 1.until(10).map {
    Multimedia(
        randomUUID(),
        randomUUID(),
        "https://i.picsum.photos/id/238/300/200.jpg?hmac=7sscr7sm3lmziy5VYZR9D3psQi7vltiVEaoux0v0s0M",
        Date(),
        "image/png",
        MultimediaType.IMAGE,
        150,
        randomUUID()
    )
}

// Mock data of ALbums
val album1: Album =
    Album(randomUUID(), "Gartenarbeit 2018", randomUUID(), multimediaList.shuffled().take(5))
val album2: Album = Album(
    randomUUID(), "Müllsack festmachen mit Tacker oder so, " +
        "ich weiß nicht was ich da gemacht habe", randomUUID(), multimediaList.shuffled().take(5)
)
val album3: Album =
    Album(randomUUID(), "Spaß mit Holz 2021", randomUUID(), multimediaList.shuffled().take(5))
val album4: Album = Album(
    randomUUID(),
    "Gartenarbeit 2017 mit Lulu",
    randomUUID(),
    multimediaList.shuffled().take(5)
)
val album5: Album =
    Album(randomUUID(), "Oma Geburtstag 89", randomUUID(), multimediaList.shuffled().take(5))
val album6: Album = Album(
    randomUUID(),
    "Abendessen mit Flo und Ana",
    randomUUID(),
    multimediaList.shuffled().take(5)
)
val album7: Album = Album(
    randomUUID(),
    "Manu hat Geburtstag! 2019",
    randomUUID(),
    multimediaList.shuffled().take(5)
)
val album8: Album =
    Album(randomUUID(), "Emma, du bist die Beste", randomUUID(), multimediaList.shuffled().take(5))
val album9: Album =
    Album(randomUUID(), "Wo ist unser Hund Oskar?", randomUUID(), multimediaList.shuffled().take(5))

val MOCK_ALBUMS = listOf(
    album1, album2, album3, album4, album5,
    album6, album7, album8, album9
)

fun createmockUUID(uuidIn: String): UUID {
    val s2 = uuidIn.replace("-", "")
    val uuidOut = UUID(
        BigInteger(s2.substring(0, 16), 16).toLong(),
        BigInteger(s2.substring(16), 16).toLong()
    )
    return uuidOut
}



