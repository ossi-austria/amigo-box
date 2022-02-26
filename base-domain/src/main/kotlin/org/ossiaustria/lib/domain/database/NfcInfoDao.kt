package org.ossiaustria.lib.domain.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.NfcInfoEntity
import java.util.*

@Dao
abstract class NfcInfoDao : AbstractEntityDao<NfcInfoEntity, NfcInfoEntity>() {

    @Query("DELETE FROM nfcs")
    abstract override suspend fun deleteAll()

    @Query("DELETE FROM nfcs where nfcTagId = :id")
    abstract override suspend fun deleteById(id: UUID)

    @Transaction
    @Query("SELECT * FROM nfcs ORDER BY createdAt ASC")
    abstract override fun findAll(): Flow<List<NfcInfoEntity>>

    @Transaction
    @Query("SELECT * FROM nfcs where nfcTagId = :id")
    abstract override fun findById(id: UUID): Flow<NfcInfoEntity>

}