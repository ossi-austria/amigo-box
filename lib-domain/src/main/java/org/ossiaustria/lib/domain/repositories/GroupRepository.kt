package org.ossiaustria.lib.domain.repositories

import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.GroupApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.GroupDao
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.*
import org.ossiaustria.lib.domain.models.Group
import timber.log.Timber
import java.util.*


interface GroupRepository {

    fun getAllGroups(): Flow<Outcome<List<Group>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getGroup(id: UUID): Flow<Outcome<Group>>

}

internal class GroupRepositoryImpl(
    private val groupApi: GroupApi,
    private val groupDao: GroupDao,
    private val personDao: PersonDao,
    private val dispatcherProvider: DispatcherProvider
) : GroupRepository,
    SingleAndCollectionStore<GroupEntity, GroupEntityWithMembers, Group>(groupDao) {

    override suspend fun fetchOne(id: UUID): Group = groupApi.get(id)
    override suspend fun fetchAll(): List<Group> = groupApi.getAll()


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

    override fun readAllItems(): Flow<List<Group>> {
        return groupDao.findAll().map {
            try {
                it.toGroupList()
            } catch (e: Exception) {
                Timber.e(e, "Store4 cannot read collection")
                emptyList<Group>()
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllGroups(): Flow<Outcome<List<Group>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Group>> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getGroup(id: UUID): Flow<Outcome<Group>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Group> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }
}



