package com.example.primacountries.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.primacountries.ui.repository.CountriesRepository
import com.example.primacountries.ui.repository.database.model.Country
import com.example.primacountries.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountriesViewModel(application: Application) : AndroidViewModel(application) {

    var _countriesList= MediatorLiveData<Resource<ArrayList<Country>>>()
    val countriesList: LiveData<Resource<ArrayList<Country>>> get() = _countriesList
    private var sourceList: LiveData<Resource<ArrayList<Country>>> = MutableLiveData()

    private val countriesRepository: CountriesRepository = CountriesRepository.getInstance(application)

    init {
        getCountries(false)
    }

    private fun getCountries(forceRefresh: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _countriesList.removeSource(sourceList) // We make sure there is only one source of livedata (allowing us properly refresh)
        withContext(Dispatchers.IO) { sourceList = countriesRepository.getCountries(forceRefresh = forceRefresh) }
        _countriesList.addSource(sourceList) {
            _countriesList.value = it
        }
    }
}