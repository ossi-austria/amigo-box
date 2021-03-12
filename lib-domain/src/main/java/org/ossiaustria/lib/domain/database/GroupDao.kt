package org.ossiaustria.lib.domain.database

import androidx.room.*
import org.ossiaustria.lib.domain.database.entities.*

@Dao
internal interface GroupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<GroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GroupEntity)

    @Insert
    suspend fun insert(item: MembershipEntity)

    @Insert
    suspend fun insert(item: MembershipPersonRef)

    @Delete
    suspend fun delete(item: GroupEntity)

    @Transaction
    @Query("SELECT * FROM groups")
    suspend fun findAll(): List<GroupWithMembershipsAndPersons>

    @Transaction
    @Query("SELECT * FROM persons")
    suspend fun findPersonsWithMembership(): List<PersonWithMembership>
}