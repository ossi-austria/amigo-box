package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Multimedia
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.ossiaustria.lib.domain.repositories.MultimediaRepository
import org.ossiaustria.lib.domain.services.ServiceMocks.HER_PERSON_ID
import org.ossiaustria.lib.domain.services.ServiceMocks.MY_PERSON_ID
import timber.log.Timber
import java.util.*
import java.util.UUID.randomUUID

interface MultimediaService {
    fun getOne(id: UUID): Flow<Resource<Multimedia>>
    fun getAll(): Flow<Resource<List<Multimedia>>>
    fun findWithOwner(ownerId: UUID): Flow<Resource<List<Multimedia>>>
}

class MockMultimediaServiceImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val multimediaRepository: MultimediaRepository,
) : MultimediaService {

    private fun mockMultimedia(
        id: UUID = randomUUID(),
        ownerId: UUID = HER_PERSON_ID,
        createdAt: Date = Date(),
    ) = Multimedia(
        id = id,
        createdAt = createdAt,
        ownerId = ownerId,
        filename = "https://en.wikipedia.org/wiki/Image#/media/File:Image_created_with_a_mobile_phone.png",
        contentType = "",
        type = MultimediaType.IMAGE
    )

    override fun getOne(id: UUID): Flow<Resource<Multimedia>> =
        flowOf(Resource.success(mockMultimedia()))

    override fun getAll(): Flow<Resource<List<Multimedia>>> = getAll {
        (1..200).map {
            val ownerId = if (it.even) MY_PERSON_ID else HER_PERSON_ID
            mockMultimedia(ownerId = ownerId)
        }
    }

    override fun findWithOwner(
        ownerId: UUID ,
    ): Flow<Resource<List<Multimedia>>> = getAll {
        (1..4).map { mockMultimedia(ownerId = ownerId) }
    }

    fun getAll(creator: () -> List<Multimedia>): Flow<Resource<List<Multimedia>>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)

}