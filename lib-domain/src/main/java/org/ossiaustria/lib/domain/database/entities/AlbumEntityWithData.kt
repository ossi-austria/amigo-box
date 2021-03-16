package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Multimedia

data class AlbumEntityWithData(

    @Embedded
    val album: AlbumEntity,

    @Relation(
        entity = MultimediaEntity::class,
        parentColumn = "albumId",
        entityColumn = "albumId",
    )
    val items: List<MultimediaEntity>
) : AbstractEntity

fun AlbumEntityWithData.toAlbum(): Album {

    return Album(
        id = this.album.albumId,
        name = this.album.name,
        ownerId = this.album.ownerId,
        items = this.items.map(MultimediaEntity::toMultimedia),
        createdAt = this.album.createdAt,
        updatedAt = this.album.updatedAt,
    )
}

fun List<AlbumEntityWithData>.toAlbumList(): List<Album> {
    return this.map { it.toAlbum() }
}

fun Album.toAlbumEntity() = AlbumEntity(
    albumId = this.id,
    name = this.name,
    ownerId = this.ownerId,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun Album.toMultimediaEntityList(): List<MultimediaEntity> =
    this.items.map(Multimedia::toMultimediaEntity)