package com.homindolentrahar.meersani.model

import com.google.gson.annotations.SerializedName

data class ProductionCompanies(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)