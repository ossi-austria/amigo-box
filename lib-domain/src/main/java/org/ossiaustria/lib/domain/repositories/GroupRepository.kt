package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    fun getAllGroups(): Flow<Resource<List<Group>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getGroup(id: UUID): Flow<Resource<Group>>

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
    override suspend fun defaultFetchAll(): List<Group> = groupApi.getAll()

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
    override fun getAllGroups(): Flow<Resource<List<Group>>> = flow {
        defaultCollectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Group>> ->
                transformResponseToOutcome(response, onNewData = { Resource.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getGroup(id: UUID): Flow<Resource<Group>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Group> ->
                transformResponseToOutcome(response, onNewData = { Resource.loading() })
            }
    }

    override fun transform(item: GroupEntityWithMembers): Group = item.toGroup()

}



