package org.ossiaustria.lib.domain.database

import androidx.room.*
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.GroupWithMembershipsAndPersons

@Dao
internal interface GroupDao {
    @Insert
    suspend fun insertAll(items: List<GroupEntity>)

    @Insert
    suspend fun insert(item: GroupEntity)

    @Delete
    suspend fun delete(item: GroupEntity)

    @Transaction
    @Query("SELECT * FROM groups")
    suspend fun findAll(): List<GroupWithMembershipsAndPersons>
}