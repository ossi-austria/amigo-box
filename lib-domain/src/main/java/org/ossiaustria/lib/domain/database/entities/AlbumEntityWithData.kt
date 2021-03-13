package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.Album

internal data class AlbumEntityWithData(

    @Embedded
    val album: AlbumEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = "ownerId",
        entityColumn = "personId",
    )
    val owner: PersonEntity,

    @Relation(
        entity = MultimediaEntity::class,
        parentColumn = "albumId",
        entityColumn = "albumId",
    )
    val items: List<MultimediaEntity>
) : AbstractEntity

internal fun AlbumEntityWithData.toAlbum(): Album {

    return Album(
        id = this.album.albumId,
        name = this.album.name,
        owner = this.owner.toPerson(),
        items = this.items.map(MultimediaEntity::toMultimedia),
        createdAt = this.album.createdAt,
        updatedAt = this.album.updatedAt,
    )
}

