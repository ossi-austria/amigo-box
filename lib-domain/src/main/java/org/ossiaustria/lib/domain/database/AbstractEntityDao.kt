package org.ossiaustria.lib.domain.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.AbstractEntity
import java.util.*

abstract class AbstractEntityDao<IN, OUT> where  IN : AbstractEntity {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAll(items: List<@JvmSuppressWildcards IN>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(item: IN)

    @Delete
    abstract suspend fun delete(item: IN)

    // must be overridden!
    abstract suspend fun deleteById(id: UUID)

    // must be overridden!
    abstract suspend fun deleteAll()

    // must be overridden!
    abstract fun findAll(): Flow<List<@JvmSuppressWildcards OUT>>

    // must be overridden!
    abstract fun findById(id: UUID): Flow<OUT>

}