package org.ossiaustria.lib.domain.database

import androidx.room.*
import org.ossiaustria.lib.domain.database.entities.MemberEntity

@Dao
internal interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<MemberEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MemberEntity)

    @Delete
    suspend fun delete(item: MemberEntity)

    @Query("DELETE FROM members")
    suspend fun deleteAll()

}