package org.ossiaustria.lib.domain.database.entities

import androidx.room.Embedded
import androidx.room.Relation
import org.ossiaustria.lib.domain.models.AlbumShare

 data class AlbumShareEntityWithData(

     @Embedded
     val albumShare: AlbumShareEntity,

     @Relation(
         entity = PersonEntity::class,
         parentColumn = "senderId",
         entityColumn = "personId",
     )
     val sender: PersonEntity,

     @Relation(
         entity = PersonEntity::class,
         parentColumn = "receiverId",
         entityColumn = "personId",
     )
     val receiver: PersonEntity,

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

