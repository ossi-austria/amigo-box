package org.ossiaustria.lib.domain.models

import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.util.*


data class NfcTag(
    val id: UUID,
    val creator: Person,
    val owner: Person,
    val type: NfcTagType,
    val linkedPerson: Person? = null,
    val linkedMedia: Multimedia? = null,
    val linkedAlbum: Album? = null
)