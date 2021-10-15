package org.ossiaustria.lib.domain.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ossiaustria.lib.commons.DispatcherProvider
import org.ossiaustria.lib.domain.api.PersonApi
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.database.PersonDao
import org.ossiaustria.lib.domain.database.entities.PersonEntity
import org.ossiaustria.lib.domain.database.entities.toPerson
import org.ossiaustria.lib.domain.database.entities.toPersonEntity
import org.ossiaustria.lib.domain.models.Person
import timber.log.Timber
import java.util.*

interface PersonRepository {

    fun getAllPersons(refresh: Boolean = false): Flow<Resource<List<Person>>>

    @ExperimentalCoroutinesApi
    fun getPerson(id: UUID, refresh: Boolean = false): Flow<Resource<Person>>
}

internal class PersonRepositoryImpl(
    private val groupApi: PersonApi,
    private val personDao: PersonDao,
    dispatcherProvider: DispatcherProvider
) : PersonRepository,
    SingleAndCollectionStore<PersonEntity, PersonEntity, Person>(personDao, dispatcherProvider) {

    override suspend fun fetchOne(id: UUID): Person = groupApi.get(id)
    override suspend fun defaultFetchAll(): List<Person> = groupApi.getAll()

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

    override fun defaultReadAll(): Flow<List<PersonEntity>> = personDao.findAll()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getAllPersons(refresh: Boolean): Flow<Resource<List<Person>>> = flow {
        listTransform(
            defaultCollectionStore.stream(newRequest(key = "all", refresh = refresh))
        )
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getPerson(id: UUID, refresh: Boolean): Flow<Resource<Person>> = flow {
        itemTransform(
            singleStore.stream(newRequest(key = id, refresh = refresh))
        )
    }

    override fun transform(item: PersonEntity): Person = item.toPerson()
}



