package com.example.primacountries.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.coroutineContext

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture Guide](https://developer.android.com/arch).
 *
 * @param <ResultType>
 * @param <RequestType>
 */

abstract class NetworkBoundResource<ResultType, RequestType> {

    /**
     * The final result LiveData
     */
    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResource<ResultType, RequestType> {
        withContext(Dispatchers.Main) { result.value =
            Resource.justLoading()
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val dbResult = loadFromDb()
            if (shouldFetch(dbResult)) {
                try {
                    fetchFromNetwork()
                } catch (e: Exception) {
                    setValue(Resource.success(dbResult))
                }
            } else {
                setValue(Resource.success(dbResult))
            }
        }
        return this
    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) result.postValue(newValue)
    }

    /**
     * Fetch the data from network and persist into DB and then
     * send it back to UI.
     */
    private suspend fun fetchFromNetwork() {
        val apiResponse = createCall()
        setValue(Resource.justLoading())
        if (apiResponse.isSuccessful) {
            saveCallResult(processResponse(apiResponse))
            setValue(Resource.success(loadFromDb()))
        } else {
            onFetchFailed()
            setValue(Resource.error((apiResponse.message())))
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected abstract suspend fun saveCallResult(item: RequestType)

    private fun processResponse(response: Response<RequestType>) = response.body()!!

    protected abstract suspend fun createCall(): Response<RequestType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun loadFromDb(): ResultType

}