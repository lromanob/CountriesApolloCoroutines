package com.example.primacountries.ui.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.primacountries.ui.repository.database.model.Country
import com.example.primacountries.ui.repository.restapi.ApiHandler
import com.example.primacountries.ui.repository.restapi.ApiInterfaces
import com.example.primacountries.utils.NetworkBoundResource
import com.example.primacountries.utils.Resource
import it.rcs.database.datasource.CountriesLocalDataSource
import it.rcs.restapi.data.source.api.response.CallResponse
import retrofit2.Response

class CountriesRepository(private var application: Application) {

    private val webservice: ApiInterfaces = ApiHandler.getInstance(application).getApiService()

    private val localDataSource: CountriesLocalDataSource = CountriesLocalDataSource()

    /**
     * Suspended function that will get a list of [Country]
     * whether in cache (SQLite) or via network (API).
     * [NetworkBoundResource] is responsible to handle this behavior.
     */
    suspend fun getCountries(forceRefresh: Boolean = false) : LiveData<Resource<ArrayList<Country>>> {
        return object : NetworkBoundResource<ArrayList<Country>, ArrayList<Country>>() {
            override suspend fun saveCallResult(item: ArrayList<Country>) {
                localDataSource.insertCountriesDB(item)
            }

            override suspend fun createCall(): Response<ArrayList<Country>> =
                webservice.getCountries()

            override fun shouldFetch(data: ArrayList<Country>?): Boolean = forceRefresh || data == null
                    || data.isEmpty()

            override suspend fun loadFromDb(): ArrayList<Country> = localDataSource.getCountriesList()

        }.build().asLiveData()
    }

    companion object {
        @Volatile
        private var INSTANCE: CountriesRepository? = null

        fun getInstance(
            application: Application
        ): CountriesRepository {
            return INSTANCE
                ?: synchronized(this) {
                INSTANCE
                    ?: CountriesRepository(
                        application
                    ).also { INSTANCE = it }
            }
        }
    }
}
