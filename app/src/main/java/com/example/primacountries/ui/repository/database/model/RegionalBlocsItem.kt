package com.example.primacountries.ui.repository.database.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegionalBlocsItem(

	@Json(name="otherNames")
	val otherNames: List<Any?>? = null,

	@Json(name="acronym")
	val acronym: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="otherAcronyms")
	val otherAcronyms: List<Any?>? = null
)