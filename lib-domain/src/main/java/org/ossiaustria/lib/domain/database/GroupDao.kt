package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.GroupEntityWithMembers
import java.util.*

@Dao
abstract class GroupDao : AbstractEntityDao<GroupEntity, GroupEntityWithMembers>() {

    @Query("DELETE FROM groups")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM groups where groupId = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM groups ORDER BY name ASC")
    abstract override fun findAll(): Flow<List<GroupEntityWithMembers>>

    @Transaction
    @Query("SELECT * FROM groups where groupId = :id")
    abstract override fun findById(id: UUID): Flow<GroupEntityWithMembers>

}