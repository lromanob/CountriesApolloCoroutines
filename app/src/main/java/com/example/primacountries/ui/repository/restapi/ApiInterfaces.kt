package com.example.primacountries.ui.repository.restapi

import com.example.primacountries.ui.repository.database.model.Country
import it.rcs.restapi.data.source.api.response.CallResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterfaces {

    // Newsstand
    @Headers("Content-Type: application/json")
    @GET("region/europe")
    suspend fun getCountries(): Response<ArrayList<Country>>
}
