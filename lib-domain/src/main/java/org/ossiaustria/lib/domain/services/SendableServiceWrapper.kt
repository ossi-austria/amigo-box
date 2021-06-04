package org.ossiaustria.lib.domain.services

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Sendable
import timber.log.Timber


class SendableServiceWrapper<S : Sendable>(
    private val ioDispatcher: CoroutineDispatcher,
) {

    fun getOne(creator: () -> S): Flow<Resource<S>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)

    fun getAll(creator: () -> List<S>): Flow<Resource<List<S>>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)


    fun findWithPersons(creator: () -> List<S>): Flow<Resource<List<S>>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)

    fun findWithSender(creator: () -> List<S>): Flow<Resource<List<S>>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)

    fun findWithReceiver(creator: () -> List<S>): Flow<Resource<List<S>>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)

    fun markAsSent(creator: () -> S): Flow<Resource<S>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)

    fun markAsRetrieved(creator: () -> S): Flow<Resource<S>> = flow {
        emit(Resource.loading())
        val mock = creator()
        emit(Resource.success(mock))
    }.catch {
        Timber.e(it)
        emit(Resource.failure(it))
    }.flowOn(ioDispatcher)
}
