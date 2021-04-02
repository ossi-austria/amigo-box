package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.Flow
import org.ossiaustria.lib.domain.database.entities.SendableEntity
import java.util.*

abstract class SendableDao<ENTITY, WRAPPER> :
    AbstractEntityDao<ENTITY, WRAPPER>() where  ENTITY : SendableEntity {

    // must be overridden!
    abstract fun findAllOldest(): Flow<List<@JvmSuppressWildcards WRAPPER>>

    // must be overridden!
    abstract fun findBySender(id: UUID): Flow<List<@JvmSuppressWildcards WRAPPER>>

    // must be overridden!
    abstract fun findByReceiver(id: UUID): Flow<List<@JvmSuppressWildcards WRAPPER>>

}