package org.ossiaustria.amigobox.ui.albums

import android.provider.ContactsContract
import org.ossiaustria.amigobox.R
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import java.math.BigInteger
import java.util.*

val mockUUID: UUID = createmockUUID("cbe8e603-6ab4-4b43-8674-10b80ed805cf")

// mock data for Multimedia
val multimedia01: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/412/300/200.jpg?hmac=V61oXlLTpOiO1Zmt6LEJiOS-62Ap2ww8gv07K1dqpnk", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia02: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/944/200/300.jpg?hmac=7n5IlapvjzzLSNeQqn4V2syM31KgmM1E9uyKQYNTz_g", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia03: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/74/300/200.jpg?hmac=IM380cEW_nZc4bHbAl0eley2wSjiCNXFIRdZUiHt-tw", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia11: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/944/200/300.jpg?hmac=7n5IlapvjzzLSNeQqn4V2syM31KgmM1E9uyKQYNTz_g", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia12: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/572/300/200.jpg?hmac=arCF-6rVTmF5GK6914DnDLgrkA3-G6DCYUIfRoGRysY", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia13: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/238/300/200.jpg?hmac=7sscr7sm3lmziy5VYZR9D3psQi7vltiVEaoux0v0s0M", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia21: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/925/300/200.jpg?hmac=jK4g2axQh9jQgTORk3GDOE-9xIe2c9viFDeCz3EdXlQ", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia22: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/74/300/200.jpg?hmac=IM380cEW_nZc4bHbAl0eley2wSjiCNXFIRdZUiHt-tw", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())
val multimedia23: Multimedia = Multimedia(mockUUID, System.currentTimeMillis(),
    123456890, 123456890, UUID.randomUUID(), UUID.randomUUID(),
    UUID.randomUUID(), "https://i.picsum.photos/id/238/300/200.jpg?hmac=7sscr7sm3lmziy5VYZR9D3psQi7vltiVEaoux0v0s0M", "local-url", MultimediaType.IMAGE,
    1234567890, UUID.randomUUID())


// Mock Data for item Lists of Mulitmedia
val items1: List<Multimedia> = listOf(
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03,
    multimedia01, multimedia02, multimedia03)
val items2: List<Multimedia> = listOf(multimedia11, multimedia12, multimedia13)
val items3: List<Multimedia> = listOf(multimedia21, multimedia22, multimedia23)

// Mock data of ALbums
val album1: Album = Album(mockUUID, "Gartenarbeit 2018", UUID.randomUUID(), items1)
val album2: Album = Album(mockUUID, "Müllsack festmachen mit Tacker oder so, " +
    "ich weiß nicht was ich da gemacht habe", UUID.randomUUID(), items2)
val album3: Album = Album(mockUUID, "Spaß mit Holz 2021", UUID.randomUUID(), items3)
val album4: Album = Album(mockUUID, "Gartenarbeit 2017 mit Lulu", UUID.randomUUID(), items1)
val album5: Album = Album(mockUUID, "Oma Geburtstag 89", UUID.randomUUID(), items2)
val album6: Album = Album(mockUUID, "Abendessen mit Flo und Ana", UUID.randomUUID(), items3)
val album7: Album = Album(mockUUID, "Manu hat Geburtstag! 2019", UUID.randomUUID(), items1)
val album8: Album = Album(mockUUID, "Emma, du bist die Beste", UUID.randomUUID(), items2)
val album9: Album = Album(mockUUID, "Wo ist unser Hund Oskar?", UUID.randomUUID(), items3)

fun createmockUUID(uuidIn: String): UUID{
    val s2 = uuidIn.replace("-", "")
    val uuidOut = UUID(
        BigInteger(s2.substring(0, 16), 16).toLong(),
        BigInteger(s2.substring(16), 16).toLong()
    )
    return uuidOut
}



