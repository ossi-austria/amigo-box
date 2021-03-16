package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.NfcTag


internal data class NfcTagEntityWithData(

    @Embedded
    val nfcTag: NfcTagEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = "personId",
        entityColumn = "creatorId",
    )
    val creator: PersonEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = "personId",
        entityColumn = "ownerId",
    )
    val owner: PersonEntity,

    ) : AbstractEntity

internal fun NfcTagEntityWithData.toNfcTag(): NfcTag {

    return NfcTag(
        id = this.nfcTag.nfcTagId,
        creator = this.creator.toPerson(),
        owner = this.owner.toPerson(),
        type = this.nfcTag.type,
    )
}

