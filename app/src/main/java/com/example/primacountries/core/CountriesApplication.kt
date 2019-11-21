package com.example.primacountries.core

import android.app.Application
import io.paperdb.Paper

class CountriesApplication : Application() {

    override fun onCreate() {

        super.onCreate()
        Paper.init(applicationContext)
    }
}
