package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.AlbumShare

data class AlbumShareEntityWithData(

    @Embedded
    val albumShare: AlbumShareEntity,

    @Relation(
        entity = AlbumEntity::class,
        parentColumn = "albumId",
        entityColumn = "albumId",
    )
    val album: AlbumEntityWithData,

    )

internal fun AlbumShareEntityWithData.toAlbumShare(): AlbumShare {

    return AlbumShare(
        id = this.albumShare.albumId,
        album = this.album.toAlbum(),
        receiverId = this.albumShare.receiverId,
        senderId = this.albumShare.senderId,
        createdAt = this.albumShare.createdAt,
        retrievedAt = this.albumShare.retrievedAt,
        sendAt = this.albumShare.sendAt,
    )
}


internal fun AlbumShare.toAlbumShareEntity(): AlbumShareEntity {

    return AlbumShareEntity(
        id = this.id,
        createdAt = this.createdAt,
        sendAt = this.sendAt,
        retrievedAt = this.retrievedAt,
        senderId = this.senderId,
        receiverId = this.receiverId,
        albumId = this.album.id
    )
}