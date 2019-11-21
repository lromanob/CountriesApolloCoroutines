package com.example.primacountries.ui.repository.database.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrenciesItem(

	@Json(name="symbol")
	val symbol: String? = null,

	@Json(name="code")
	val code: String? = null,

	@Json(name="name")
	val name: String? = null
)