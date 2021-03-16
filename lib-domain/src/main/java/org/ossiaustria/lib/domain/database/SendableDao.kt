package org.ossiaustria.lib.domain.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.ossiaustria.lib.domain.database.entities.SendableEntity
import java.util.*

abstract class SendableDao<T> where  T : SendableEntity {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAll(items: List<@JvmSuppressWildcards T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: T)

    @Delete
    abstract suspend fun delete(item: T)

    abstract suspend fun deleteAll()

    abstract suspend fun findAll(): List<@JvmSuppressWildcards T>

    abstract suspend fun findAllOldest(): List<@JvmSuppressWildcards T>

    abstract suspend fun findById(id: UUID): T

    abstract suspend fun findBySender(id: UUID): List<@JvmSuppressWildcards T>

    abstract suspend fun findByReceiver(id: UUID): List<@JvmSuppressWildcards T>


}