package org.ossiaustria.lib.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey
    val id: Long,

    val postId: Long,
    val title: String,

    val text: String,
)