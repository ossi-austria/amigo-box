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
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.PersonApi
import org.ossiaustria.lib.domain.common.Outcome
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import org.ossiaustria.lib.domain.database.entities.toPerson
import org.ossiaustria.lib.domain.database.entities.toPersonEntity
import org.ossiaustria.lib.domain.models.Person
import timber.log.Timber
import java.util.*


interface PersonRepository {

    fun getAllPersons(): Flow<Outcome<List<Person>>>

    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    fun getPerson(id: UUID): Flow<Outcome<Person>>

}

internal class PersonRepositoryImpl(
    private val groupApi: PersonApi,
    private val personDao: PersonDao,
    private val dispatcherProvider: DispatcherProvider
) : PersonRepository,
    SingleAndCollectionStore<PersonEntity, PersonEntity, Person>(personDao) {

    override suspend fun fetchOne(id: UUID): Person = groupApi.get(id)
    override suspend fun fetchAll(): List<Person> = groupApi.getAll()

    override suspend fun writeItem(item: Person) {
        try {
            personDao.insert(item.toPersonEntity())
        } catch (e: Exception) {
            Timber.e(e, "Store4 cannot write item")
        }
    }

    override fun readItem(id: UUID): Flow<Person> =
        withFlowItem(personDao.findById(id)) {
            it.toPerson()
        }

    override fun readAllItems(): Flow<List<Person>> =
        withFlowCollection(personDao.findAll()) {
            it.toPerson()
        }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getAllPersons(): Flow<Outcome<List<Person>>> = flow {
        collectionStore.stream(StoreRequest.cached(key = "all", refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<List<Person>> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun getPerson(id: UUID): Flow<Outcome<Person>> = flow {
        singleStore.stream(StoreRequest.cached(key = id, refresh = true))
            .flowOn(dispatcherProvider.io())
            .collect { response: StoreResponse<Person> ->
                transformResponseToOutcome(response, onNewData = { Outcome.loading() })
            }
    }
}



