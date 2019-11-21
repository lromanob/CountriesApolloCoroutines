package it.rcs.database.datasource

import android.app.Application
import com.example.primacountries.ui.repository.database.model.Country
import io.paperdb.Paper

/**
 * Local storage for Designer News login related data, implemented using Room
 */
class CountriesLocalDataSource() {

    fun insertCountriesDB(countries: ArrayList<Country>) {
        Paper.book().write("countries", countries )
    }

    fun getCountriesList(): ArrayList<Country> {
        return Paper.book().read("countries", arrayListOf())
    }
}
