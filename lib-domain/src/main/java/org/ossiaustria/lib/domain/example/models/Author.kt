package org.ossiaustria.lib.domain.example.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey
    val id: Long,
    val name: String,
)