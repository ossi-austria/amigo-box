package org.ossiaustria.lib.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.ossiaustria.lib.domain.models.Person
import java.util.*

@Entity(tableName = "persons")
internal data class PersonEntity(
    @PrimaryKey
    val personId: UUID,

    val name: String,
    val email: String,
)

internal fun PersonEntity.toPerson(): Person {

    return Person(
        id = this.personId,
        email = this.email,
        name = this.name
    )
}