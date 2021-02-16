package org.ossiaustria.lib.domain.example.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey
    val id: Long,

    val authorId: Long,

    val title: String,

    val text: String,

    )

/**
 * One to many relationships can be modelled like this
 *
 */
data class PostWithComments(
    @Embedded val post: Post,
    @Relation(
        parentColumn = "id",
        entityColumn = "postId"
    )
    val comments: List<Comment>
)