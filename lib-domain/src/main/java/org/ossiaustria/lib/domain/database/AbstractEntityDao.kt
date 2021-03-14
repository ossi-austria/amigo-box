package org.ossiaustria.lib.domain.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.AbstractEntity
import java.util.*

 abstract class AbstractEntityDao<T, X> where  T : AbstractEntity {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAll(items: List<@JvmSuppressWildcards T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: T)

    @Delete
    abstract suspend fun delete(item: T)

    // must be overridden!
    abstract suspend fun deleteById(id: UUID)

    // must be overridden!
    abstract suspend fun deleteAll()

    // must be overridden!
    abstract fun findAll(): Flow<List<@JvmSuppressWildcards X>>

    // must be overridden!
    abstract fun findById(id: UUID): Flow<X>

}