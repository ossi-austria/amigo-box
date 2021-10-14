package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.GroupEntity
import org.ossiaustria.lib.domain.database.entities.GroupEntityWithMembers
import org.ossiaustria.lib.domain.database.entities.toGroup
import org.ossiaustria.lib.domain.database.entities.toGroupEntity
import org.ossiaustria.lib.domain.database.entities.toPersonEntityList
import org.ossiaustria.lib.domain.models.Group
import timber.log.Timber
import java.util.*

interface GroupRepository {

    fun getAllGroups(refresh: Boolean = false): Flow<Resource<List<Group>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getGroup(id: UUID, refresh: Boolean = false): Flow<Resource<Group>>
}

internal class GroupRepositoryImpl(
    private val groupApi: GroupApi,
    private val groupDao: GroupDao,
    private val personDao: PersonDao,
    dispatcherProvider: DispatcherProvider
) : GroupRepository,
    SingleAndCollectionStore<GroupEntity, GroupEntityWithMembers, Group>(
        groupDao,
        dispatcherProvider
    ) {

    override suspend fun fetchOne(id: UUID): Group = groupApi.get(id)
    override suspend fun defaultFetchAll(): List<Group> = groupApi.getOwn()

    override suspend fun writeItem(item: Group) {
        try {
            groupDao.insert(item.toGroupEntity())
            personDao.insertAll(item.toPersonEntityList())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item")
        }
    }

    override fun readItem(id: UUID): Flow<Group> {
        return groupDao.findById(id).map { it.toGroup() }
    }

    override fun defaultReadAll(): Flow<List<GroupEntityWithMembers>> {
        return groupDao.findAll()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllGroups(refresh: Boolean): Flow<Resource<List<Group>>> = flow {
        listTransform(
            defaultCollectionStore.stream(newRequest(key = "all", refresh = refresh))
        )
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getGroup(id: UUID, refresh: Boolean): Flow<Resource<Group>> = flow {
        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: GroupEntityWithMembers): Group = item.toGroup()

}



