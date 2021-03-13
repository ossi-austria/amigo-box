package org.ossiaustria.lib.domain.database

import androidx.room.*
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.GroupEntityWithMembers
import java.util.*

@Dao
internal interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<GroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GroupEntity)

    @Delete
    suspend fun delete(item: GroupEntity)

    @Query("DELETE FROM groups")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM groups")
    suspend fun findAll(): List<GroupEntityWithMembers>

    @Transaction
    @Query("SELECT * FROM groups where groupId = :id")
    suspend fun findById(id: UUID): GroupEntityWithMembers

}