package org.ossiaustria.lib.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey
    val id: UUID,
    val name: String,
    val email: String,
)